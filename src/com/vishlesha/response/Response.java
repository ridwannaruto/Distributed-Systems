package com.vishlesha.response;

import com.vishlesha.dataType.Node;
import com.vishlesha.error.Error;

/**
 * Created by ridwan on 1/1/16.
 */

abstract public class Response {
    private Node recipientNode;
    private Node senderNode;
    private String responseMessage;
    private int responseCode;
    private boolean fail;
    private Error error;
    private long timestamp;

    public static final int KEY_MSG_LENGTH = 0;
    public static final int KEY_RESPONSE_TYPE = 1;
    public static final int KEY_IP_ADDRESS = 2;
    public static final int KEY_PORT_NUM = 3;

    public Response(String responseMessage, Node senderNode) {
        this.responseMessage = responseMessage;
        this.senderNode = senderNode;
    }

    public Response() {
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Error getError() {
        return error;
    }

    void setError(Error error) {
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

    public String getResponseMessage() {
        return responseMessage;
    }

    public boolean isFail() {
        return fail;
    }

    void setFail(boolean fail) {
        this.fail = fail;
    }

    void appendMsgLength() {
        responseMessage = String.format("%04d", responseMessage.length() + 4) + responseMessage;
    }

    void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
