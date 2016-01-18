package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.Response;

/**
 * Created by ridwan on 1/2/16.
 */
public class JoinRequestHandler {

    private static final int RESPOND_CODE_JOIN_SUCCESS = 0;
    private static final int RESPOND_CODE_JOIN_ERROR = 9999;

    JoinResponse response;

    public void handle(JoinRequest request){
        // add as neighbor
        Node neighbor = request.getServerNode();
        try{
            GlobalState.addNeighbor(neighbor);
            sendResponse(new JoinResponse(RESPOND_CODE_JOIN_SUCCESS));
        }catch (IllegalStateException ex){
            sendResponse(new JoinResponse(RESPOND_CODE_JOIN_ERROR));
        }

    }

    private void sendResponse(Response response){
        Client client = new Client();
        client.sendUDPResponse(response);
    }
}
