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
    SearchResponse response;

    public SearchRequestHandler(SearchRequest request){
        //TODO LOGIC
       String query = request.getFileName();
       FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
       Client client = new Client();// Get from global state
       Map<String,List<List<String>>> allFileList = fileIpMapping.searchForFile(query);



      for(String ip: allFileList.keySet()) {
         SearchRequest newRequest = request;
         Node node = new Node();
         node.setIpaddress(ip);
         node.setPortNumber(1000); //Todo change port
         newRequest.setRecepientNode(node);
         client.sendUDPRequest(request, CallBack.emptyCallback);// Cahnge callback?
      }
       //If the user posses any related file

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
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }
}
