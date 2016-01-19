package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.Response;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/2/16.
 */
public class JoinRequestHandler {

    private static final int RESPOND_CODE_JOIN_SUCCESS = 0;
    private static final int RESPOND_CODE_JOIN_ERROR = 9999;

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    public void handle(JoinRequest request){
        // add as neighbor
        Node neighbor = request.getServerNode();
        try{
            GlobalState.addNeighbor(neighbor);
            log.info(this.getClass() +" : new neighbour joined " + neighbor.toString());
            sendResponse(new JoinResponse(RESPOND_CODE_JOIN_SUCCESS));
        }catch (IllegalStateException ex){
            log.severe(this.getClass() + ": neighbour already exists " + neighbor.toString() );
            sendResponse(new JoinResponse(RESPOND_CODE_JOIN_ERROR));
        }

    }

    private void sendResponse(Response response){
        Client client = new Client();
        client.sendUDPResponse(response);
    }
}
