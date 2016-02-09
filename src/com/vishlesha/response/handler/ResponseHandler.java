package com.vishlesha.response.handler;

import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.response.*;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
public class ResponseHandler {

    private static final int KEY_MSG_LENGTH = 0;
    private static final int KEY_RESPONSE_TYPE = 1;

    private static final String RESPONSE_TYPE_REGISTER = "REGOK";
    private static final String RESPONSE_TYPE_JOIN = "JOINOK";
    private static final String RESPONSE_TYPE_LEAVE = "LEAVEOK";
    private static final String RESPONSE_TYPE_SEARCH = "SEROK";
    private static final String RESPONSE_TYPE_FILE_SHARE = "FILESOK";

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    public void handle(String responseMessage, Node sender) {

        String[] token = responseMessage.split(" ");
        int lengthOfMessage = Integer.valueOf(token[KEY_MSG_LENGTH]);

        if (lengthOfMessage != responseMessage.length()) {
            log.severe(this.getClass() + " : incorrect message length");
            sendResponse(new ErrorResponse());
        } else {
            if (token[KEY_RESPONSE_TYPE].equals(RESPONSE_TYPE_REGISTER)) {
                RegisterResponse registerResponse = new RegisterResponse(responseMessage, sender);
                RegisterResponseHandler registerResponseHandler = new RegisterResponseHandler();
                registerResponseHandler.handle(registerResponse);

            } else if (token[KEY_RESPONSE_TYPE].equals(RESPONSE_TYPE_JOIN)) {
                JoinResponse joinResponse = new JoinResponse(responseMessage, sender);
                JoinResponseHandler joinResponseHandler = new JoinResponseHandler();
                joinResponseHandler.handle(joinResponse);

            } else if (token[KEY_RESPONSE_TYPE].equals(RESPONSE_TYPE_LEAVE)) {
                LeaveResponse leaveResponse = new LeaveResponse(responseMessage, sender);
                LeaveResponseHandler leaveResponseHandler = new LeaveResponseHandler();
                leaveResponseHandler.handle(leaveResponse);

            } else if (token[KEY_RESPONSE_TYPE].equals(RESPONSE_TYPE_FILE_SHARE)) {
                FileShareResponse fileShareResponse = new FileShareResponse(responseMessage);
                FileShareResponseHandler fileShareResponseHandler = new FileShareResponseHandler();
                fileShareResponseHandler.handle(fileShareResponse);

            } else if (token[KEY_RESPONSE_TYPE].equals(RESPONSE_TYPE_SEARCH)) {

                SearchResponse searchResponse = new SearchResponse(responseMessage, sender);
                SearchResponseHandler searchResponseHandler = new SearchResponseHandler();
                searchResponseHandler.handle(searchResponse);

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
