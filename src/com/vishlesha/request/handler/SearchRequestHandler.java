package com.vishlesha.request.handler;

import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.request.SearchRequest;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.SearchResponse;

/**
 * Created by ridwan on 1/2/16.
 */
public class SearchRequestHandler {

    private static final int RESPOND_CODE_SEARCH_UNREACHABLE = 9999;
    private static final int RESPOND_CODE_SEARCH_ERROR = 9998;

    SearchResponse response;

    public SearchRequestHandler(SearchRequest request){
        //TO DO LOGIC

        //setResponse(new SearchResponse());
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }
}
