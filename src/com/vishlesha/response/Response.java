package com.vishlesha.response;

import com.vishlesha.dataType.Node;
import com.vishlesha.error.Error;

/**
 * Created by ridwan on 1/1/16.
 */

abstract public class Response {
    protected Node recipientNode;
    protected Node senderNode;
    protected String responseMessage;
    protected int responseCode;
    protected boolean fail;
    protected Error error;

    public static final int KEY_MSG_LENGTH = 0;
    public static final int KEY_RESPONSE_TYPE = 1;
    public static final int KEY_IP_ADDRESS = 2;
    public static final int KEY_PORT_NUM = 3;

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

    public Node getSenderNode() {
        return senderNode;
    }

    public void setSenderNode(Node senderNode) {
        this.senderNode = senderNode;
    }

    public Node getRecipientNode() {
        return recipientNode;
    }

    public void setRecipientNode(Node recipientNode) {
        this.recipientNode = recipientNode;
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
