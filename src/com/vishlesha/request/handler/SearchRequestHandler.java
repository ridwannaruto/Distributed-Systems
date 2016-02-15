package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.FileIpMapping;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.SearchRequest;
import com.vishlesha.response.SearchResponse;
import com.vishlesha.timer.task.ForgetRequestTask;
import com.vishlesha.timer.task.PrintResponseTask;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/2/16.
 */
public class SearchRequestHandler {

    private static final int RESPOND_CODE_SEARCH_UNREACHABLE = 9999;
    private static final int RESPOND_CODE_SEARCH_ERROR = 9998;
    private static final int RESPOND_CODE_SEARCH_SUCCESS = 0;
    private static final int SEARCH_REQUEST_PORT = 1055;
    private static final int MAX_NUMBER_OF_HOPS = 10;
    private static final int Number_OF_FORWARDS = 3;

    private static final int TIME_TO_FORGET_REQUEST = 3000;
    private SearchResponse response;

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    private final Logger networkLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);


    public synchronized void handle(SearchRequest request) {

        GlobalState.incrementReceivedRequestCount();
        if (GlobalState.isSearchRequestAlreadyProcessed(request)) {
            networkLog.warning("duplicate request " + request.getRequestMessage());
            sendLocalResult(request);
            return;
        }

        GlobalState.rememberRequest(request);
        TimerTask forgetRequestTask = new ForgetRequestTask(request);
        Timer timer = new Timer();
        timer.schedule(forgetRequestTask, TIME_TO_FORGET_REQUEST);

        try {
            sendLocalResult(request);
            forwardSearchRequest(request);

        } catch (Exception ex) {
            log.severe(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void forwardSearchRequest(SearchRequest request) {

        Node initiator = request.getInitialNode();
        Node sender = request.getSenderNode();
        String query = request.getQuery();

        int forwardCount = 0;
        int noOfHops = request.getNoOfHops();

        if (noOfHops < MAX_NUMBER_OF_HOPS) {
            FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
            final Client client = GlobalState.getClient();
            Map<Node, List<String>> allFileList = fileIpMapping.searchForFile(query); // Neighbors with results for the query

            List<Node> sentNodes = new ArrayList<>();

            int newNoOfHops = noOfHops + 1;
            for (Node node : allFileList.keySet()) {
                // ignore if same node as the sender
                if (node.equals(sender) || node.equals(initiator) || node.equals(GlobalState.getLocalServerNode())) {
                    continue;
                }

                //If the user posses any related file respond to user
                //Forward the request to all neighbours with a result for the query
                forwardCount++;
                GlobalState.incrementForwardedRequestCount();

                // since client is async we need new request objects for each call
                Node recipient = new Node(node.getIpaddress(), node.getPortNumber());
                SearchRequest newRequest = new SearchRequest(initiator, recipient, query, newNoOfHops);
                networkLog.info("Filelist forward: " + sender.toString() + " to " + recipient.toString());
                client.sendUDPRequest(newRequest, true);
                sentNodes.add(node);
            }

            // If already sent to 3 or more neighbors, this will  terminate
            Map<Node,Integer> sortedMap = sortByValues(GlobalState.getNeighborCountList());
            for (Node neighbor : sortedMap.keySet()) {
                //ignore if node already sent
                if(sentNodes.contains(neighbor)){
                    continue;
                }

                // ignore if same node as the sender
                if (neighbor.equals(sender) || neighbor.equals(initiator) || neighbor.equals(GlobalState.getLocalServerNode())) {
                    continue;
                }

                if (forwardCount >= Number_OF_FORWARDS) {
                    networkLog.info(this.getClass() + " : Forward count reached...");
                    break;
                } else {
                    forwardCount++;
                    GlobalState.incrementForwardedRequestCount();
                    request.setNoOfHops(newNoOfHops);

                    Node recipient = new Node(neighbor.getIpaddress(), neighbor.getPortNumber());
                    SearchRequest newRequest = new SearchRequest(initiator, recipient, query, newNoOfHops);
                    networkLog.info("Locality forward: " + sender.toString() + " to " + recipient.toString());
                    networkLog.info(sender.toString() + " forwarding to : " + neighbor.toString());
                    client.sendUDPRequest(newRequest, true);
                    sentNodes.add(neighbor);
                }
            }
        } else {
            networkLog.info(this.getClass() + " : Reached Hops limit");
        }
    }


    public void initiateSearch(String searchQuery) {
        SearchRequest ser = new SearchRequest(GlobalState.getLocalServerNode(), GlobalState.getLocalServerNode(),
                searchQuery, 0);
        ser.setSenderNode(GlobalState.getLocalServerNode());
        ser.setTimestamp(new Date().getTime());

        System.out.println("\nLocal search result\n----------------------");
        List<String> localResult = getLocalResult(ser);
        if (localResult.isEmpty()) {
            System.out.println("no matching result found");
        } else {
            for (String aLocalResult : localResult) {
                System.out.println(aLocalResult);
            }
        }

        System.out.printf("\n\n\nsearching for file in network ......\n\n");
        GlobalState.setCurrentSearchingRequest(ser);
        forwardSearchRequest(ser);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimerTask printTask = new PrintResponseTask();
        printTask.run();
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }

    public List<String> getLocalResult(SearchRequest request) {
        FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
        Map<Node, List<String>> fileMap = fileIpMapping.searchForFile(request.getQuery());
        List<String> fileList = fileMap.get(GlobalState.getLocalServerNode());
        if (fileList == null) {
            fileList = new ArrayList<>();
        }
        return fileList;
    }

    private void sendLocalResult(SearchRequest request) {
        Node source = request.getSenderNode();
        Node initiator = request.getInitialNode();

        try {
            List<String> fileList = getLocalResult(request);
            SearchResponse response = new SearchResponse(fileList.size(), request.getNoOfHops(), fileList);
            final Client client = GlobalState.getClient();

            //send response to Sender
            response.setRecipientNode(request.getSenderNode());
            GlobalState.incrementAnsweredRequestCount();
            client.sendUDPResponse(response);
            log.info("local search result sent to sender");

            if (!source.equals(initiator)) {
                //send response to Initiator
                response.setRecipientNode(request.getInitialNode());
                GlobalState.incrementAnsweredRequestCount();
                client.sendUDPResponse(response);
                log.info("local search result sent to initiator");
            }

        } catch (Exception ex) {
            log.severe(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static Map<Node,Integer> sortByValues(Map<Node,Integer> map) {
        List<Map.Entry<Node,Integer>> list = new LinkedList<>(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator<Map.Entry<Node,Integer>>() {
            public int compare(Map.Entry<Node,Integer> o1, Map.Entry<Node,Integer> o2) {
                return   o1.getValue().compareTo(o2.getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap<Node,Integer> sortedHashMap = new LinkedHashMap<>();
        for (Iterator<Map.Entry<Node,Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Node,Integer> entry = it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
