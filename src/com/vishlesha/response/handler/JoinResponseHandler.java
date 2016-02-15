package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.network.Util;
import com.vishlesha.request.FileShareRequest;
import com.vishlesha.response.JoinResponse;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
class JoinResponseHandler {

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    private final Logger netLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    public void handle(JoinResponse joinResponse) {
        Node newNeighbour = joinResponse.getSenderNode();

        try {
            String key = "JOIN-" + newNeighbour.getIpaddress();

            // we don't know the listening port of the JOINOK sender; get it from the pending request
            newNeighbour.setPortNumber(GlobalState.getResponsePendingRequest(key).getRecipientNode().getPortNumber());

            GlobalState.removeResponsePendingRequest(key);
            netLog.info("removed request from pending list");

            GlobalState.removeRegisteredNode(joinResponse.getSenderNode());
            log.info("removed Node from registered nodes list");

        } catch (Exception ex) {
            netLog.warning("could not remove request from pending list");
            netLog.severe(Util.getStackTrace(ex));
        }

        try {
            GlobalState.addNeighbor(newNeighbour);
            log.info("new neighbour added " + newNeighbour.toString());
            GlobalState.getClient().sendUDPRequest(new FileShareRequest(newNeighbour,
                    GlobalState.getLocalFiles()), true);
            log.info("local file list sent to " + newNeighbour.toString());

        } catch (IllegalStateException ex) {
            log.warning("node already exists " + newNeighbour.toString());
        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }
    }
}
