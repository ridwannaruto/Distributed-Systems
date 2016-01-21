package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.*;
import com.vishlesha.response.SearchResponse;
import com.vishlesha.search.FileIpMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    SearchResponse response;

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    Logger networkLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);


    public void handle(SearchRequest request) {
        if (GlobalState.isSearchRequestAlreadyProcessed(request)) {
            networkLog.warning("duplicate request " + request.getRequestMessage());
            return;
        }
        GlobalState.rememberRequest(request);
        Node initiator = request.getInitialNode();
        Node sender = request.getSenderNode();
        String query = request.getFileName();

        try{
            FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
            final Client client = GlobalState.getClient();// Get from global state
            Map<Node, List<List<String>>> allFileList = fileIpMapping.searchForFile(query); // Neighbors with results for the query
            Map<Node, List<String>> neighbors = GlobalState.getNeighbors();
            int noOfHops = request.getNoOfHops();
            int forwardCount = 0;
            sendLocalResult(request);

            if (noOfHops < MAX_NUMBER_OF_HOPS) {
                int newNoOfHops = noOfHops + 1;
                for (Node node : allFileList.keySet()) {
                    // ignore if same node as the sender
                    if (node.equals(sender) || node.equals(initiator) || node.equals(GlobalState.getLocalServerNode())) {
                        continue;
                    }

                    //If the user posses any related file respond to user
                    //Forward the request to all neighbours with a result for the query
                    forwardCount++;
                    Node recipientNode = new Node(node.getIpaddress(), node.getPortNumber());
                    request.setNoOfHops(newNoOfHops);
                    request.setRecipientNode(recipientNode);
                    client.sendUDPRequest(request);// Change callback?
                }
                // If already sent to 3 or more neighbors, this will  terminate
                // TODO sort neighbors based on NumberoFNeigbors
                for (Node neighbor : neighbors.keySet()) {
                    // ignore if same node as the sender
                    if (neighbor.equals(sender) || neighbor.equals(initiator) || neighbor.equals(GlobalState.getLocalServerNode()) ) {
                        continue;
                    }
                    if (forwardCount >= Number_OF_FORWARDS) {
                        networkLog.info(this.getClass() + " : Forward count reached...");
                        break;
                    } else {
                        forwardCount++;
                        request.setNoOfHops(newNoOfHops);
                        Node node = new Node();
                        node.setIpaddress(neighbor.getIpaddress());
                        node.setPortNumber(neighbor.getPortNumber()); //Todo change port
                        request.setRecipientNode(node);
                        request.updateRequestMessage();
                        networkLog.info(sender.toString() + " forwarding to : " + neighbor.toString());
                        client.sendUDPRequest(request);
                    }
                }
            } else {
                networkLog.info(this.getClass() + " : Reached Hops limit");
            }
            // TODO remove this ONLY after a suitable time has passed! (on actual network)
            GlobalState.forgetRequest(request);

        }catch (Exception ex){
            log.severe(ex.getMessage());
            ex.printStackTrace();
        }

    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }

    public List<String> getLocalResult(SearchRequest request){
        FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
        Map<Node, List<List<String>>> fileMap  = fileIpMapping.searchForFile(request.getFileName());
        List<List<String>> fileListList;
        List<String> fileList = new ArrayList<String>();
        try{
            fileListList = fileMap.get(GlobalState.getLocalServerNode());
            StringBuilder s = new StringBuilder();
            if (fileListList != null){
                for (List<String> stringList : fileListList) {
                    for (String string : stringList) {
                        s.append(string);
                        s.append(" ");
                    }
                    fileList.add(s.toString().trim());
                    //System.out.println(s);
                }
            }
        }catch(Exception ex){
            log.severe(ex.getMessage());
        }
        return fileList;
    }

    private void sendLocalResult(SearchRequest request) {

        try{
            List<String > fileList = getLocalResult(request);
            SearchResponse response = new SearchResponse(fileList.size(), request.getNoOfHops(), fileList);
            final Client client = new Client();
            //send response to Sender
            response.setRecipientNode(request.getSenderNode());
            if (!request.getRecipientNode().equals(GlobalState.getLocalServerNode()))
            client.sendUDPResponse(response);
            log.info("local search result sent to sender");

            //send response to Initiator
            response.setRecipientNode(request.getInitialNode());
            if (!request.getRecipientNode().equals(GlobalState.getLocalServerNode()))
            client.sendUDPResponse(response);
            log.info("local search result sent to initiator");

        }catch (Exception ex){
            log.severe(ex.getMessage());
            ex.printStackTrace();
        }


    }
}
