package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.LeaveResponse;
import com.vishlesha.response.Response;

/**
 * Created by ridwan on 1/2/16.
 */
public class LeaveRequestHandler {

    private static final int RESPOND_CODE_LEAVE_SUCCESS = 0;
    private static final int RESPOND_CODE_LEAVE_ERROR = 9999;

    LeaveResponse response;

    public void handle(LeaveRequest request){
        // remove neighbor
        Node neighbor = request.getServerNode();
        try{
            GlobalState.removeNeighbor(neighbor);
            sendResponse(new JoinResponse(RESPOND_CODE_LEAVE_SUCCESS));
        }catch (IllegalStateException ex){
            sendResponse(new JoinResponse(RESPOND_CODE_LEAVE_ERROR));
        }
        sendResponse(new LeaveResponse(RESPOND_CODE_LEAVE_SUCCESS));
    }

    private void sendResponse(Response response){
        Client client = new Client();
        client.sendUDPResponse(response);
    }
}
