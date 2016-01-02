package com.vishlesha.errorHandler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.request.Request;

/**
 * Created by ridwan on 1/2/16.
 */
public class ErrorHandler {

    protected Request request;
    protected String handleMessage;
    protected boolean retry;

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public String getHandleMessage() {
        return handleMessage;
    }

    public void setHandleMessage(String handleMessage) {
        this.handleMessage = handleMessage;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
