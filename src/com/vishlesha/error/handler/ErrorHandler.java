package com.vishlesha.error.handler;

import com.vishlesha.error.Error;
import com.vishlesha.error.RegisterError;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.request.Request;

/**
 * Created by ridwan on 1/2/16.
 */
public class ErrorHandler {

    public void handleError(Error error) {
        if (error.getClass().equals(RegisterError.class)) {
            RegisterErrorHandler registerErrorHandler = new RegisterErrorHandler();
            registerErrorHandler.handleErrorResponse(error);
        }
    }

    public void handleNodeUnreachable(Request request) {
        if (request.getClass().equals(JoinRequest.class)) {
            JoinErrorHandler joinErrorHandler = new JoinErrorHandler();
            joinErrorHandler.handleNodeUnreachable(request);

        } else if (request.getClass().equals(LeaveRequest.class)) {
            LeaveErrorHandler leaveErrorHandler = new LeaveErrorHandler();
            leaveErrorHandler.handleNodeUnreachable(request);
        }
    }
}
