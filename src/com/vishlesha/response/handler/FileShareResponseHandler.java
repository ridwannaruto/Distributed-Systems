package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.response.FileShareResponse;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
class FileShareResponseHandler {

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    private final Logger netLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    public void handle(FileShareResponse fileShareResponse) {
        Node neighbour = fileShareResponse.getRecipientNode();
        try {
            String key = "FILES-" + neighbour.getIpaddress();
            GlobalState.removeResponsePendingRequest(key);
            netLog.info("removed request from pending list");
        } catch (Exception ex) {
            netLog.warning("could not remove request from pending list");
        }

        try {
            GlobalState.addNeighborFiles(neighbour, fileShareResponse.getFiles());
            log.info("added files from neighbor " + fileShareResponse.getSenderNode().toString());
        } catch (IllegalStateException ex) {
            log.warning("files sent by unkown neighbor " + neighbour.toString());
        } catch (RuntimeException ex) {
            log.warning("duplicate files sent by " + neighbour.toString());

        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }

    }

}
