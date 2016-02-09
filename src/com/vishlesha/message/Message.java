package com.vishlesha.message;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */

abstract public class Message {
    private Node recipientNode;
    private Node senderNode;
    private String message;


    public static final int KEY_MSG_LENGTH = 0;
    public static final int KEY_RESPONSE_TYPE = 1;
    public static final int KEY_IP_ADDRESS = 2;
    public static final int KEY_PORT_NUM = 3;



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

    public String getMessage() {
        return message;
    }


    void appendMsgLength() {
        message = String.format("%04d", message.length() + 4) + message;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
