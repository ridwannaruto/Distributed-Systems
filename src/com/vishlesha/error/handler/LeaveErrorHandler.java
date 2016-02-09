package com.vishlesha.error.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.request.Request;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/21/16.
 */
class LeaveErrorHandler {

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);


    public void handleNodeUnreachable(Request request) {
        Node unreachableNode = request.getRecipientNode();
        try {
            GlobalState.removeNeighbor(unreachableNode);
            log.info("Removed neighbor from list " + unreachableNode.toString());

        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }

    }
}
