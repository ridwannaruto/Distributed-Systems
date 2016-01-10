package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.SearchError;
import com.vishlesha.network.CallBack;
import com.vishlesha.network.Client;
import com.vishlesha.request.Request;
import com.vishlesha.request.SearchRequest;
import com.vishlesha.request.SearchResponseRequest;
import com.vishlesha.response.Response;
import com.vishlesha.response.SearchResponse;
import com.vishlesha.search.FileIpMapping;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ridwan on 1/2/16.
 */
public class SearchResponseRequestHandler {

    private static final int RESPOND_CODE_SEARCH_UNREACHABLE = 9999;
    private static final int RESPOND_CODE_SEARCH_ERROR = 9998;
    private static final int RESPOND_CODE_SEARCH_SUCCESS = 0;
    private static final int SEARCH_REQUEST_PORT = 1055;
    private static final int MAX_NUMBER_OF_HOPS =  10;
    private static final int Number_OF_FORWARDS = 3;
    SearchResponse response;


    public SearchResponseRequestHandler(SearchResponseRequest request) {
       //TODO LOGIC
       String responseMessage = request.getRequestMessage();
       System.out.println("Search Response Message from Handler :" + responseMessage);
       String[] token = responseMessage.split(" ");
       int responseCode = Integer.valueOf(token[2]);

       if (token[1].equals("SEROK") && responseCode == 0) {
          if (!GlobalState.isTestMode())
             System.out.println(GlobalConstant.MSG_SEARCH_NORESULT);
       } else if (token[1].equals("SEROK") && responseCode < 9000) {
          if (!GlobalState.isTestMode())
             System.out.println(GlobalConstant.SUCCESS_MSG_SEARCH);
       } else {
          SearchError searchError = new SearchError(responseMessage, request.getSender());
          if (!GlobalState.isTestMode())
             System.out.println("Search Error: " + searchError.getErrorMessage());
       }

       setResponse(getSearchResponse(responseCode, request.getNoOfHops(), request.getResults()));
    }

      public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }

   private SearchResponse getSearchResponse(int responseCode, int noOfHops, List<String> fileList){
      String fileNameList ="";
      for (int i=0; i<fileList.size(); i++)
         fileNameList += " " + fileList.get(i);
      String responseMessage = " SEROK " + responseCode + " " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber() + " " + noOfHops + fileNameList;
      responseMessage = String.format("%04d", responseMessage.length()+4) + responseMessage;
      response.setResponseMessage(responseMessage);
      response.appendMsgLength();
      return  response;
   }
}
