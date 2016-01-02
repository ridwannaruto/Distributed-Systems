package com.vishlesha.request;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class Request {
    protected Node recepientNode;
    protected String requestMessage;

    protected static final int KEY_MSG_LENGTH = 0;
    protected static final int KEY_REQ_TYPE = 1;
    protected static final int KEY_IP_ADDRESS = 2;
    protected static final int KEY_PORT_NUM = 3;

    public Node getRecepientNode() {
        return recepientNode;
    }

    public void setRecepientNode(Node recepientNode) {
        this.recepientNode = recepientNode;
    }

    public String getRequestMessage(){
        return requestMessage;
    }

    protected void appendMsgLength(){
        requestMessage = String.format("%04d", requestMessage.length()+4) + requestMessage;
    }


}
