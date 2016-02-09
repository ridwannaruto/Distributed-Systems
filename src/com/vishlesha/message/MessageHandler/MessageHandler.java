package com.vishlesha.message.MessageHandler;

import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.message.HeartBeatMessage;
import com.vishlesha.network.Client;
import com.vishlesha.response.*;
import com.vishlesha.response.handler.*;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
public class MessageHandler {

    private static final int KEY_MSG_LENGTH = 0;
    private static final int KEY_RESPONSE_TYPE = 1;

    private static final String MSG_TYPE_HEAR_BEAT = "HBTMSG";

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    public void handle(String responseMessage, Node sender) {

        String[] token = responseMessage.split(" ");
        int lengthOfMessage = Integer.valueOf(token[KEY_MSG_LENGTH]);

        if (lengthOfMessage != responseMessage.length()) {
            log.severe(this.getClass() + " : incorrect message length");
            sendResponse(new ErrorResponse());
        } else {
            if (token[KEY_RESPONSE_TYPE].equals(MSG_TYPE_HEAR_BEAT)) {
                HeartBeatMessage heartBeatMessage = new HeartBeatMessage(responseMessage, sender);
                HeartBeatMessageHandler heartBeatMessageHandler = new HeartBeatMessageHandler();
                heartBeatMessageHandler.handle(heartBeatMessage);

            } else {
                log.severe(this.getClass() + " : unknown message format");
                sendResponse(new ErrorResponse());
            }
        }
    }

    private void sendResponse(Response response) {
        Client client = new Client();
        client.sendUDPResponse(response);
    }

}
