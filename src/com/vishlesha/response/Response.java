package com.vishlesha.response;

import com.vishlesha.dataType.Node;
import com.vishlesha.error.Error;

/**
 * Created by ridwan on 1/1/16.
 */

abstract public class Response {
    protected Node respondNode;
    protected String responseMessage;
    protected int responseCode;
    protected boolean fail;
    protected Error error;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Node getRespondNode() {
        return respondNode;
    }

    public void setRespondNode(Node respondNode) {
        this.respondNode = respondNode;
    }

    public String getResponseMessage(){
        return responseMessage;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public void appendMsgLength(){
        responseMessage = String.format("%04d", responseMessage.length()+4) + responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
