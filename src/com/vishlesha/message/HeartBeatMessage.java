package com.vishlesha.message;

import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/1/16.
 */
public class HeartBeatMessage extends Message {

    private static  final int KEY_MSG = 2;

    private final Logger logger = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    public HeartBeatMessage(String responseMessage, Node senderNode) {
        setSenderNode(senderNode);
        String[] token = responseMessage.split(" ");
        setMessage(token[KEY_MSG]);
    }

    public HeartBeatMessage(int neighborCount) {
        String message = " HBTMSG " + neighborCount;
        setMessage(message);
        appendMsgLength();
    }
}
