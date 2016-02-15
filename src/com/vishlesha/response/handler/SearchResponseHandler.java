package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.error.SearchError;
import com.vishlesha.log.AppLogger;
import com.vishlesha.response.SearchResponse;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
class SearchResponseHandler {

    private static final int RESPOND_CODE_SEARCH_UNREACHABLE = 9999;
    private static final int RESPOND_CODE_SEARCH_ERROR = 9998;
    private static final int RESPOND_CODE_SEARCH_SUCCESS = 0;
    private static final int SEARCH_REQUEST_PORT = 1055;
    private static final int MAX_NUMBER_OF_HOPS = 10;
    private static final int Number_OF_FORWARDS = 3;
    private SearchResponse response;

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    private final Logger netLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    public synchronized void handle(SearchResponse response) {
        response.setTimestamp(new Date().getTime());
        String key = "SER-" + response.getSenderNode().getIpaddress();
        GlobalState.addSearchResponse(response);

        try {
            GlobalState.removeResponsePendingRequest(key);
            netLog.info("removed request from pending list");
        } catch (Exception ex) {
            netLog.warning("could not remove request from pending list");
        }
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }
}

