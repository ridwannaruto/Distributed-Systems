package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.FileShareRequest;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.request.SearchRequest;
import com.vishlesha.response.ErrorResponse;
import com.vishlesha.response.Response;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/2/16.
 */
public class RequestHandler {

    private static final int KEY_MSG_LENGTH = 0;
    private static final int KEY_REQ_TYPE = 1;

    private static final String REQ_TYPE_JOIN = "JOIN";
    private static final String REQ_TYPE_LEAVE = "LEAVE";
    private static final String REQ_TYPE_SEARCH = "SER";
    private static final String REQ_TYPE_FILES = "FILES";

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    public void handle(String requestMessage, Node sender) {

        String[] token = requestMessage.split(" ");
        int lengthOfMessage = Integer.valueOf(token[KEY_MSG_LENGTH]);

        if (lengthOfMessage != requestMessage.length()) {
            log.severe(this.getClass() + " : incorrect message length");
            sendErrorResponse(new ErrorResponse());
        } else {
            if (token[KEY_REQ_TYPE].equals(REQ_TYPE_JOIN)) {
                JoinRequest joinRequest = new JoinRequest(requestMessage);
                joinRequest.setSenderNode(sender);
                JoinRequestHandler joinRequestHandler = new JoinRequestHandler();
                joinRequestHandler.handle(joinRequest);

            } else if (token[KEY_REQ_TYPE].equals(REQ_TYPE_LEAVE)) {
                LeaveRequest leaveRequest = new LeaveRequest(requestMessage);
                leaveRequest.setSenderNode(sender);
                LeaveRequestHandler leaveRequestHandler = new LeaveRequestHandler();
                leaveRequestHandler.handle(leaveRequest);

            } else if (token[KEY_REQ_TYPE].equals(REQ_TYPE_FILES)) {
                FileShareRequest fileShareRequest = new FileShareRequest(requestMessage);
                fileShareRequest.setSenderNode(sender);
                FileShareRequestHandler fileShareRequestHandler = new FileShareRequestHandler();
                fileShareRequestHandler.handle(fileShareRequest);

            } else if (token[KEY_REQ_TYPE].equals(REQ_TYPE_SEARCH)) {
                SearchRequest searchRequest = new SearchRequest(requestMessage);
                searchRequest.setSenderNode(sender);
                SearchRequestHandler searchRequestHandler = new SearchRequestHandler();
                searchRequestHandler.handle(searchRequest);

            } else {
                log.severe(this.getClass() + " : unknown message format: " + requestMessage);
                sendErrorResponse(new ErrorResponse());
            }
        }
    }

    private void sendErrorResponse(Response response) {
        GlobalState.getClient().sendUDPResponse(response);
    }

}
