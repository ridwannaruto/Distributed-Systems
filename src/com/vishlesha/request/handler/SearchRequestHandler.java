package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.CallBack;
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
    private static final int MAX_NUMBER_OF_HOPS =  10;
    private static final int Number_OF_FORWARDS = 3;
    SearchResponse response;

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    Logger networkLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);
    public void handle(SearchRequest request){
        if (GlobalState.isRequestAlreadyHandled(request)) {
            log.info(this.getClass() + " : Ignoring duplicate request");
            return;
        }
        GlobalState.rememberRequest(request);

       Node initiator = request.getInitiator();
       Node sender = request.getSenderNode();

       String query = request.getFileName();
       FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
       final Client client = GlobalState.getClient();// Get from global state
       Map<Node,List<List<String>>> allFileList = fileIpMapping.searchForFile(query); // Neighbors with results for the query
       Map<Node, List<String>> neighbors = GlobalState.getNeighbors();
       int noOfHops =  request.getNoOfHops();
       int forwardCount = 0;

      if(noOfHops < MAX_NUMBER_OF_HOPS) {
            int newNoOfHops = noOfHops + 1;
            for (Node node : allFileList.keySet()) {
                // ignore if same node as the sender
                if(node.equals(sender)) {
                    continue;
                }

                //If the user posses any related file respond to user
               if(node.equals(GlobalState.getLocalServerNode())){
                  List<List<String>> fileList = allFileList.get(GlobalState.getLocalServerNode());
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
                  // Here reply the initiator with a new message.
                  String formatted_Message = searchResponseMessage(files.size(),noOfHops,files);
                  replyToInitiator(GlobalState.getLocalServerNode(),initiator ,noOfHops, files, formatted_Message);

               //setResponse(new SearchResponse(RESPOND_CODE_SEARCH_SUCCESS, request.getNoOfHops(),files));
               }else { //Forward the request to all neighbours with a result for the query
                  forwardCount++;
                  Node recepientNode = new Node(node.getIpaddress(),node.getPortNumber());
                  String requestMessage = request.getRequestMessage();
                  String[] token = requestMessage.split(" ");

                  Node initNode = new Node();
                  initNode.setIpaddress(token[2]);
                  initNode.setPortNumber(Integer.valueOf(token[3]));

                  String fileName = token[4];

                  SearchRequest newRequest = new SearchRequest(request.getInitiator(),fileName,newNoOfHops);
                  newRequest.setRecipientNode(recepientNode);
                  client.sendUDPRequest(newRequest);// Change callback?
               }
            }
            // If already sent to 3 or more neighbors, this will  terminate
            // TODO sort neighbors based on NumberoFNeigbors
            for(Node neighbor : neighbors.keySet()){

                // ignore if same node as the sender
                if(neighbor.equals(sender)) {
                    continue;
                }

                if (forwardCount >= Number_OF_FORWARDS ){
                    networkLog.info(this.getClass() + " : Forward count reached...");
                  break;
               }else{
                  forwardCount++;
                  final SearchRequest newRequest = request;
                  newRequest.setNoOfHops(newNoOfHops);
                  Node node = new Node();
                  node.setIpaddress(neighbor.getIpaddress());
                  node.setPortNumber(neighbor.getPortNumber()); //Todo change port

                  newRequest.setRecipientNode(node);
                    networkLog.info(this.getClass() + " : " + sender +  " forwarding to : " + neighbor);
                  client.sendUDPRequest(newRequest);
               }
            }
      }else{
          networkLog.info(this.getClass() + " : Reached Hops limit");
      }
        // TODO remove this ONLY after a suitable time has passed! (on actual network)
        GlobalState.forgetRequest(request);
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }

   private String searchResponseMessage(int responseCode, int noOfHops, List<String> fileList){
      String fileNameList ="";
      for (int i=0; i<fileList.size(); i++)
         fileNameList += " " + fileList.get(i);
      String responseMessage = " SEROK " + responseCode + " " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber() + " " + noOfHops + fileNameList;
      responseMessage = String.format("%04d", responseMessage.length()+4) + responseMessage;
      //setResponseMessage(responseMessage);
      //appendMsgLength();
      return  responseMessage;
   }

   private void replyToInitiator(Node sender, Node recepient, int numberOfHops, List<String> files, String formattedMeesage){
      SearchResponse response = new SearchResponse(files.size(),numberOfHops,files);
      final Client client = new Client();
      response.setSenderNode(sender);
      //response.setNoOfHops(numberOfHops);
      response.setRecipientNode(recepient);
      //response.setRequestMessage(formattedMeesage);
      //response.setResults(files);
      networkLog.info(this.getClass() + " : Reply to initiator");
      client.sendUDPResponse(response);
   }
}
