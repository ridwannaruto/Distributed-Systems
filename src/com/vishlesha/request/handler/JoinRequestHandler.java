package com.vishlesha.request.handler;

import com.vishlesha.request.JoinRequest;
import com.vishlesha.response.JoinResponse;

/**
 * Created by ridwan on 1/2/16.
 */
public class JoinRequestHandler {

    private static final int RESPOND_CODE_JOIN_SUCCESS = 0;
    private static final int RESPOND_CODE_JOIN_ERROR = 9999;

    JoinResponse response;

    public JoinRequestHandler(JoinRequest request){
        //TO DO LOGIC

        setResponse(new JoinResponse(RESPOND_CODE_JOIN_SUCCESS));
    }

    public JoinResponse getResponse() {
        return response;
    }

    public void setResponse(JoinResponse response) {
        this.response = response;
    }
}
