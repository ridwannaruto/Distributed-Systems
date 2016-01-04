package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.network.CallBack;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.request.SearchRequest;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.SearchResponse;
import com.vishlesha.search.FileIpMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ridwan on 1/2/16.
 */
public class SearchRequestHandler {

    private static final int RESPOND_CODE_SEARCH_UNREACHABLE = 9999;
    private static final int RESPOND_CODE_SEARCH_ERROR = 9998;
    private static final int RESPOND_CODE_SEARCH_SUCCESS = 0;
    private static final int SEARCH_REQUEST_PORT = 1055;
    private static final int MAX_NUMBER_OF_HOPS =  10;
    private static final int Number_OF_FORWARDS = 3;
    SearchResponse response;

    public SearchRequestHandler(SearchRequest request){
        //TODO LOGIC
       String query = request.getFileName();
       FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
       Client client = new Client();// Get from global state
       Map<Node,List<List<String>>> allFileList = fileIpMapping.searchForFile(query); // Neighbors with results for the query
       Map<Node, List<String>> neighbors = GlobalState.getNeighbors();
       int noOfHops =  request.getNoOfHops();
       int forwardCount = 0;

      if(noOfHops < MAX_NUMBER_OF_HOPS) {
            int newNoOfHops = noOfHops + 1;
            for (Node node : allFileList.keySet()) {
               if(node.equals(GlobalState.getLocalServerNode().getIpaddress())){ //If the user posses any related file respond to user
                  List<List<String>> fileList = allFileList.get(
                        GlobalState.getLocalServerNode().getIpaddress());
                  List<String> files  = new ArrayList<String>();
                  StringBuilder s = new StringBuilder();
                  for(List<String> stringList : fileList){
                     for(String string : stringList){
                        s.append(string);
                        s.append(" ");
                     }
                     files.add(s.toString().trim());
                     System.out.println(s);
               }
               setResponse(new SearchResponse(RESPOND_CODE_SEARCH_SUCCESS, request.getNoOfHops(),files));
               }else { //Forward the request to all neighbours with a result for the query
                  forwardCount++;
                  final SearchRequest newRequest = request;
                  newRequest.setNoOfHops(newNoOfHops);
                  Node newNode = new Node(node.getIpaddress(),node.getPortNumber());
                  //node.setPortNumber(SEARCH_REQUEST_PORT); //Todo change port

                  newRequest.setRecepientNode(newNode);
                  CallBack callBack = new CallBack() {
                     @Override public void run(String message, Node node) {
                        new SearchRequestHandler(newRequest);
                     }
                  };

                  client.sendUDPRequest(newRequest, callBack );// Change callback?
               }
            }
            // If already sent to 3 or more neighbors, this will  terminate
            // TODO sort neighbors based on NumberoFNeigbors
            for(Node neighbor : neighbors.keySet()){
               if (forwardCount >= Number_OF_FORWARDS ){
                  System.out.println("Forward count reached...");
                  break;
               }else{
                  forwardCount++;
                  final SearchRequest newRequest = request;
                  newRequest.setNoOfHops(newNoOfHops);
                  Node node = new Node();
                  node.setIpaddress(neighbor.getIpaddress());
                  node.setPortNumber(neighbor.getPortNumber()); //Todo change port

                  newRequest.setRecepientNode(node);

                  CallBack callBack = new CallBack() {
                     @Override public void run(String message, Node node) {
                        new SearchRequestHandler(newRequest);
                     }
                  };
                  System.out.println("Forwarding to : " + neighbor);
                  client.sendUDPRequest(request, callBack);
               }
            }
      }else{
         System.out.println("Reached Hops limit");
      }
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }
}
