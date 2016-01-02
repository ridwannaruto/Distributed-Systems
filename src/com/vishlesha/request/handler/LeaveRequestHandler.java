package com.vishlesha.request.handler;

import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.LeaveResponse;

/**
 * Created by ridwan on 1/2/16.
 */
public class LeaveRequestHandler {

    private static final int RESPOND_CODE_LEAVE_SUCCESS = 0;
    private static final int RESPOND_CODE_LEAVE_ERROR = 9999;

    LeaveResponse response;

    public LeaveRequestHandler(LeaveRequest request){
        //TO DO LOGIC

        setResponse(new LeaveResponse(RESPOND_CODE_LEAVE_SUCCESS));
    }

    public LeaveResponse getResponse() {
        return response;
    }

    public void setResponse(LeaveResponse response) {
        this.response = response;
    }
}
