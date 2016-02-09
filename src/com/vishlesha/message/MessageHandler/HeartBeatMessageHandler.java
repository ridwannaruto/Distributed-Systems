package com.vishlesha.message.MessageHandler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.message.HeartBeatMessage;
import com.vishlesha.response.LeaveResponse;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
class HeartBeatMessageHandler {

    private final Logger applog = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    private final Logger netLog = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    public void handle(HeartBeatMessage heartBeatMessage) {
        Integer neighborCount = Integer.valueOf(heartBeatMessage.getMessage());
        Node neighbor = heartBeatMessage.getSenderNode();
        try {
            GlobalState.updateNeighborCount(neighbor,neighborCount);
            netLog.info("neighbor count list of " + neighbor + " updated to " + neighborCount);
        } catch (Exception ex) {
            netLog.warning("could not update neighbor count list of " + neighbor);
            ex.printStackTrace();
        }
    }
}
