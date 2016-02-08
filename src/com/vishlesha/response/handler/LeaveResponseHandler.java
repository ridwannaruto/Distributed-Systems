package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.LeaveResponse;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
public class LeaveResponseHandler {

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    Logger netLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    public void handle(LeaveResponse leaveResponse) {

        Node oldNeighbour = leaveResponse.getSenderNode();
        try {
            String key = "LEAVE-" + oldNeighbour.getIpaddress();
            GlobalState.removeResponsePendingRequest(key);
            netLog.info("removed request from pending list");
        } catch (Exception ex) {
            netLog.warning("could not remove request from pending list");
        }

        log.info(GlobalConstant.SUCCESS_MSG_LEAVE);
        try {
            GlobalState.removeNeighbor(oldNeighbour);
            log.info("Removed neighbour " + oldNeighbour.toString());

        } catch (IllegalStateException ex) {
            //TODO
            log.warning("Node doesn't exists");
        }


    }

}
