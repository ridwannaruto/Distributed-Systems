package com.vishlesha.app;

import com.vishlesha.dataType.FileIpMapping;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.Request;
import com.vishlesha.request.SearchRequest;
import com.vishlesha.response.SearchResponse;
import com.vishlesha.timer.task.HeartBeatMonitorTask;
import com.vishlesha.timer.task.HeartBeatTask;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/1/16.
 */
public class GlobalState {

    private static final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    private static String username;
    private static boolean testMode;
    private static long roundTripTime;
    private static Node localServerNode;
    private static Node bootstrapNode;

    private static final Map<Node, List<String>> neighbors = new Hashtable<>();
    private static final Map<Node,Integer> neighborCountList = new HashMap<>();

    private static final List<String> localFiles = new ArrayList<String>();

    private static final List<SearchRequest> searchRequestList = new Vector<>();
    private static final Map<String,SearchResponse> searchResponseList = new HashMap<>();
    private static final Map<String, Request> responsePendingList = new Hashtable<>();

    private static SearchRequest currentSearchingRequest;

    private static List<Node> registeredNodeList = new ArrayList<>();
    private static Semaphore heartBeatMonitorLock = new Semaphore(1);
    private static boolean neighborUnreachable = false;

    private static int receivedRequestCount = 0;
    private static int forwardedRequestCount = 0;
    private static int answeredRequestCount = 0;

    private static HeartBeatTask heartBeatTask;
    private static HeartBeatMonitorTask heartBeatMonitorTask;

    private static DatagramSocket socket;
    private static Client client;
    private static Server server;

    public static SearchRequest getCurrentSearchingRequest() {
        return currentSearchingRequest;
    }

    public static void setCurrentSearchingRequest(SearchRequest request) {
        GlobalState.currentSearchingRequest = request;
    }

    public static DatagramSocket getSocket() throws SocketException, UnknownHostException {
        if (socket == null) {
            Node localServer = getLocalServerNode();
            socket = new DatagramSocket(localServer.getPortNumber(), InetAddress.getByName(localServer.getIpaddress()));
        }
        return socket;
    }

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

    public static Map<Node, List<String>> getNeighbors() {
        return neighbors;
    }

    public static Node getLocalServerNode() {
        return localServerNode;
    }

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
        try {
            if (neighbors.containsKey(node)) {
                throw new IllegalStateException("Neighbor already joined");
            }
            neighbors.put(node, new ArrayList<String>());
            setNeighborUnreachable(false);
        } finally {
            releaseHeartBeatMonitorLock();
        }
    }

    public static void removeNeighbor(Node node) {

        if (!neighbors.containsKey(node)) {
            throw new IllegalStateException("Neighbor does not exist");
        }
        neighbors.remove(node);
        neighborCountList.remove(node);
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

    public static void addNeighborFiles(Node node, List<String> files) {
        List<String> availableFiles = neighbors.get(node);
        if (availableFiles == null) {
            throw new IllegalStateException("Files from unknown neighbor");
        } else if (!availableFiles.isEmpty()) {
            throw new RuntimeException("files already added");
        }
        availableFiles.addAll(files);
    }

    public static Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public static Server getServer() {
        if (server == null) {
            server = new Server();
        }
        return server;
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

    public static synchronized void updateNeighborCount(Node neighbor, Integer count){
       neighborCountList.put(neighbor, count);
    }

    public static Map<Node,Integer> getNeighborCountList() {
        return neighborCountList;
    }


    public static Node getBootstrapNode() {
        return bootstrapNode;
    }

    public static void setBootstrapNode(Node bootstrapNode) {
        GlobalState.bootstrapNode = bootstrapNode;
    }

    public static void acquireHeartBeatMonitorLock(){
        try {
            heartBeatMonitorLock.acquire();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void releaseHeartBeatMonitorLock(){
        heartBeatMonitorLock.release();
    }

    public static boolean isNeighborUnreachable() {
        return neighborUnreachable;
    }

    public static void setNeighborUnreachable(boolean neighborUnreachable) {
        GlobalState.neighborUnreachable = neighborUnreachable;
    }

    public static void addSearchResponse(SearchResponse searchResponse){
        if (!searchResponse.getFileList().isEmpty()){
            String fileName = searchResponse.getFileList().get(0);

            SearchRequest lastRequest = getCurrentSearchingRequest();
            if (lastRequest == null) {
                return;
            }

            // consider as a possible response, if fileName contains all searched words
            List<String> searchWords = Arrays.asList(lastRequest.getQuery().toLowerCase().split("_"));
            List<String> nameWords = Arrays.asList(fileName.toLowerCase().split("_"));

            if (nameWords.containsAll(searchWords)){
                // prevent duplicate responses from same node
                if(!searchResponseList.containsKey(searchResponse.getSenderNode().getIpaddress())){
                    searchResponseList.put(searchResponse.getSenderNode().getIpaddress(),searchResponse);
                }
            }
        }
    }

    public static void clearResponseList(){
        searchResponseList.clear();
    }

    public static Map<String, SearchResponse> getSearchResponseList() {
        return searchResponseList;
    }

    public static void startHeartBeat() {
        // do only if not initialized yet
        if (heartBeatMonitorTask == null) {
            // start monitor
            heartBeatMonitorTask = new HeartBeatMonitorTask();
            new Timer().schedule(heartBeatMonitorTask, 100);
            log.info("Heart Beat Monitor Started");

            // start beat
            heartBeatTask = new HeartBeatTask();
            new Timer().schedule(heartBeatTask, 100);
            log.info("Heart Beating Started");
        }
    }
}
