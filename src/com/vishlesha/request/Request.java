package com.vishlesha.request;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
abstract public class Request {
    private Node recipientNode; //to which node the request is sent
    private Node initialNode; // node which initiated the request / the node in the request message
    private Node senderNode; // which node is sending the request
    private String requestMessage;
    private int retryCount = 0;

    public static final int KEY_MSG_LENGTH = 0;
    public static final int KEY_REQ_TYPE = 1;
    public static final int KEY_IP_ADDRESS = 2;
    public static final int KEY_PORT_NUM = 3;


    abstract public String getHashCode();

    public int getRetryCount(){
        return retryCount;
    }

    public void incrementRetryCount(){
        retryCount++;
    }
    public Node getInitialNode() {
        return initialNode;
    }

    public void setInitialNode(Node initialNode) {
        this.initialNode = initialNode;
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

    public String getRequestMessage(){
        return requestMessage;
    }

   public void setRequestMessage(String requestMessage) {
      this.requestMessage = requestMessage;
   }

   protected void appendMsgLength(){
        requestMessage = String.format("%04d", requestMessage.length()+4) + requestMessage;
    }


}
