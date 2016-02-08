package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.LeaveResponse;
import com.vishlesha.response.Response;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/2/16.
 */
public class LeaveRequestHandler {

    private static final int RESPOND_CODE_LEAVE_SUCCESS = 0;
    private static final int RESPOND_CODE_LEAVE_ERROR = 9999;

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    public void handle(LeaveRequest request){
        // remove neighbor
        Node neighbor = request.getInitialNode();
        try{
            GlobalState.removeNeighbor(neighbor);
            log.info(this.getClass() + " : neighbour left " + neighbor.toString());
            Response leaveResponse = new LeaveResponse(RESPOND_CODE_LEAVE_SUCCESS);
            leaveResponse.setRecipientNode(neighbor);
            sendResponse(leaveResponse);
        }catch (IllegalStateException ex){
            log.severe(this.getClass() + " : neighbour doesn't exist " + neighbor.toString());
            Response leaveResponse = new LeaveResponse(RESPOND_CODE_LEAVE_SUCCESS);
            leaveResponse.setRecipientNode(neighbor);
            sendResponse(leaveResponse);
        }catch (Exception ex){
            log.severe(this.getClass() + " : error removing neighbour " + neighbor.toString());
            Response leaveResponse = new JoinResponse(RESPOND_CODE_LEAVE_ERROR);
            leaveResponse.setRecipientNode(neighbor);
            sendResponse(leaveResponse);
        }

    }

    private void sendResponse(Response response){
        Client client = new Client();
        client.sendUDPResponse(response);
    }
}
