package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.error.SearchError;
import com.vishlesha.log.AppLogger;
import com.vishlesha.response.SearchResponse;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
public class SearchResponseHandler {

    private static final int RESPOND_CODE_SEARCH_UNREACHABLE = 9999;
    private static final int RESPOND_CODE_SEARCH_ERROR = 9998;
    private static final int RESPOND_CODE_SEARCH_SUCCESS = 0;
    private static final int SEARCH_REQUEST_PORT = 1055;
    private static final int MAX_NUMBER_OF_HOPS =  10;
    private static final int Number_OF_FORWARDS = 3;
    SearchResponse response;


    public void handle(SearchResponse response) {
        Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
        Logger netLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);


        try{
            String key = "SER-" + response.getSenderNode().getIpaddress();
            GlobalState.removeResponsePendingRequest(key);
            netLog.info("removed request from pending list");
        }catch (Exception ex){
            netLog.warning("could not remove request from pending list");
        }

        String responseMessage = response.getResponseMessage();
        System.out.println("\nSearch Response Message from " + response.getSenderNode().getIpaddress() );
        System.out.println("----------------------------");

        String[] token = responseMessage.split(" ");
        int responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("SEROK") && responseCode == 0) {
            if (!GlobalState.isTestMode())
                System.out.println(GlobalConstant.MSG_SEARCH_NORESULT);
        } else if (token[1].equals("SEROK") && responseCode < 9000) {
            List<String> fileList = response.getFileList();
            if (!GlobalState.isTestMode()) {
                for (int i=0; i<fileList.size();i++){
                    System.out.printf(fileList.get(i));
                }
            }

        } else {
            SearchError searchError = new SearchError(responseMessage, response.getSenderNode());
            if (!GlobalState.isTestMode())
                System.out.println("Search Error: " + searchError.getErrorMessage());
        }

        //setResponse(getSearchResponse(responseCode, response.getNoOfHops(), response.getResults()));
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }


}

