package com.vishlesha.app;

import com.vishlesha.dataType.Node;
import com.vishlesha.network.Client;
import com.vishlesha.request.SearchRequest;
import com.vishlesha.search.FileIpMapping;

import java.util.*;

/**
 * Created by ridwan on 1/1/16.
 */
public class GlobalState {

    private static String username;
    private static boolean testMode;
    private static Node localServerNode;
    private static Map<Node, List<String>> neighbors = new HashMap<Node, List<String>>();
    private static List<String> localFiles = new ArrayList<String>();
    private static List<SearchRequest> oldRequests = new ArrayList<SearchRequest>();

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

    public static void removeNeighbor(Node n){
        neighbors.remove(n);
    }

   public static FileIpMapping getFileIpMapping() {
    //  Set<String> availableFiles = new HashSet<>();
      FileIpMapping fileIpMapping = new FileIpMapping();
      for(Node n : neighbors.keySet()){
         List<String> files = neighbors.get(n);
         for(String file: files){
           fileIpMapping.addFile(file, n);
         }
      }
      for(String file : localFiles){
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
        oldRequests.add(request);
    }

    public static boolean isRequestAlreadyHandled(SearchRequest request) {
        return oldRequests.contains(request);
    }

    public static boolean forgetRequest(SearchRequest request) {
        return oldRequests.remove(request);
    }
}
