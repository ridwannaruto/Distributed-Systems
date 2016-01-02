package com.vishlesha.request.handler;

import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.request.SearchRequest;
import com.vishlesha.response.ErrorResponse;
import com.vishlesha.response.Response;

/**
 * Created by ridwan on 1/2/16.
 */
public class RequestHandler {

    Response response;



    protected static final int KEY_MSG_LENGTH = 0;
    protected static final int KEY_REQ_TYPE = 1;

    protected static final String REQ_TYPE_JOIN = "JOIN";
    protected static final String REQ_TYPE_LEAVE = "LEAVE";
    protected static final String REQ_TYPE_SEARCH = "SER";

    public RequestHandler(String requestMessage){

        String[] token = requestMessage.split(" ");
        int lengthOfMessage = Integer.valueOf(token[KEY_MSG_LENGTH]);

        if (lengthOfMessage != requestMessage.length()){
            setResponse(new ErrorResponse());
        }

        else{
            if (token[KEY_REQ_TYPE].equals(REQ_TYPE_JOIN)){
                JoinRequest joinRequest = new JoinRequest(requestMessage);
                JoinRequestHandler joinRequestHandler = new JoinRequestHandler(joinRequest);
                setResponse(joinRequestHandler.getResponse());
            }else if (token[KEY_REQ_TYPE].equals(REQ_TYPE_LEAVE)){
                LeaveRequest leaveRequest = new LeaveRequest(requestMessage);
                LeaveRequestHandler leaveRequestHandler = new LeaveRequestHandler(leaveRequest);
                setResponse(leaveRequestHandler.getResponse());

            }else if (token[KEY_REQ_TYPE].equals(REQ_TYPE_SEARCH)){
                SearchRequest searchRequest = new SearchRequest(requestMessage);
                SearchRequestHandler searchRequestHandler = new SearchRequestHandler(searchRequest);
                setResponse(searchRequestHandler.getResponse());

            }else{
                setResponse(new ErrorResponse());
            }
        }



    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
