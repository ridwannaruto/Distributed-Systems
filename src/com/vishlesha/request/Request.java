package com.vishlesha.request;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class Request {
    protected Node recepientNode;
    protected String requestMessage;

    public Request(Node node){
        setRecepientNode(node);
    }

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
