package com.vishlesha.app;

import com.vishlesha.dataType.FileIpMapping;
import com.vishlesha.dataType.Node;
import com.vishlesha.network.Client;
import com.vishlesha.request.Request;
import com.vishlesha.request.SearchRequest;

import java.util.*;

/**
 * Created by ridwan on 1/1/16.
 */
public class GlobalState {

    private static String username;
    private static boolean testMode;
    private static long roundTripTime;
    private static Node localServerNode;
    private static final Map<Node, List<String>> neighbors = new Hashtable<>();
    private static final List<String> localFiles = new ArrayList<String>();
    private static final List<SearchRequest> searchRequestList = new Vector<>();
    private static final Map<String, Request> responsePendingList = new Hashtable<>();
    private static List<Node> registeredNodeList = new ArrayList<>();

    private static int receivedRequestCount = 0;
    private static int forwardedRequestCount = 0;
    private static int answeredRequestCount = 0;

    public static int getReceivedRequestCount() {
        return receivedRequestCount;
    }

    public static int getForwardedRequestCount() {
        return forwardedRequestCount;
    }

    public static int getAnsweredRequestCount() {
        return answeredRequestCount;
    }

    public static void incrementReceivedRequestCount() {
        receivedRequestCount++;
    }

    public static void incrementForwardedRequestCount() {
        forwardedRequestCount++;
    }

    public static void incrementAnsweredRequestCount() {
        answeredRequestCount++;
    }

    public static void setRegisteredNodeList(ArrayList<Node> list) {
        registeredNodeList = list;
    }

    public static void removeRegisteredNode(Node node) {
        registeredNodeList.remove(node);
    }

    public static Node getRegisteredNode(int index) {
        return registeredNodeList.get(index);
    }

    public static int getRegisteredNodeCount() {
        return registeredNodeList.size();
    }

    public static boolean isResponsePending(Request request) {
        return responsePendingList.containsValue(request);
    }

    public static boolean isResponsePending(String key) {
        return responsePendingList.containsKey(key);
    }

    public static void addResponsePendingRequest(String key, Request request) {
        responsePendingList.put(key, request);
    }

    public static Request getResponsePendingRequest(String key) {
        return responsePendingList.get(key);
    }

    public static void removeResponsePendingRequest(String key) {
        responsePendingList.remove(key);
    }

    public static long getRoundTripTime() {
        return roundTripTime;
    }

    public static void setRoundTripTime(long roundTripTime) {
        GlobalState.roundTripTime = roundTripTime;
    }

    private static Client client = new Client();
    //private static FileIpMapping fileIpMapping = new FileIpMapping();

    public static Map<Node, List<String>> getNeighbors() {
        return neighbors;
    }

    public static Node getLocalServerNode() {
        return localServerNode;
    }

   /*public static FileIpMapping getFileIpMapping() {
      return fileIpMapping;
   }*/

    public static void setLocalServerNode(Node localServerNode) {
        GlobalState.localServerNode = localServerNode;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        GlobalState.username = username;
    }

    public static boolean isTestMode() {
        return testMode;
    }

    public static void setTestMode(boolean testMode) {
        GlobalState.testMode = testMode;
    }

    public static void addNeighbor(Node node) {
        if (neighbors.containsKey(node)) {
            throw new IllegalStateException("Neighbor already joined");
        }
        neighbors.put(node, new ArrayList<String>());
    }

    public static void removeNeighbor(Node node) {

        if (!neighbors.containsKey(node)) {
            throw new IllegalStateException("Neighbor does not exist");
        }
        neighbors.remove(node);
    }

    public static FileIpMapping getFileIpMapping() {
        //  Set<String> availableFiles = new HashSet<>();
        FileIpMapping fileIpMapping = new FileIpMapping();
        for (Node n : neighbors.keySet()) {
            List<String> files = neighbors.get(n);
            for (String file : files) {
                fileIpMapping.addFile(file, n);
            }
        }
        for (String file : localFiles) {
            fileIpMapping.addFile(file, GlobalState.getLocalServerNode());
        }
        return fileIpMapping;

    }

    /* private static FileIpMapping getFileIpMapping(Node node, List<String> localFiles, List<String> neighborFiles) {
        FileIpMapping fileIpMapping = new FileIpMapping();
        List<String> availableFiles = neighbors.get(node);
        if (availableFiles == null) {
           throw new IllegalStateException("Files from unknown neighbor");
        }
        availableFiles.addAll(neighborFiles);
        availableFiles.addAll(localFiles);
        for(String file : availableFiles){
           fileIpMapping.addFile(file,node);
        }
        return  fileIpMapping;
     }*/
    public static void addNeighborFiles(Node node, List<String> files) {
        List<String> availableFiles = neighbors.get(node);
        if (availableFiles == null) {
            throw new IllegalStateException("Files from unknown neighbor");
        } else if (!availableFiles.isEmpty()) {
            throw new RuntimeException("files already added");
        }
        availableFiles.addAll(files);
        /* for(String file : availableFiles){
            fileIpMapping.addFile(file,node);
         }*/
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        GlobalState.client = client;
    }

    public static List<String> getLocalFiles() {
        return localFiles;
    }

    public static void addLocalFile(String file) {
        localFiles.add(file);
    }

    public static void rememberRequest(SearchRequest request) {
        searchRequestList.add(request);
    }

    public static boolean isSearchRequestAlreadyProcessed(SearchRequest request) {
        return searchRequestList.contains(request);
    }

    public static void forgetRequest(SearchRequest request) {
        searchRequestList.remove(request);
    }

}
