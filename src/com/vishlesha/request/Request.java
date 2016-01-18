package com.vishlesha.request;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class Request {
    private Node recipientNode;
    private Node serverNode;
    private Node senderNode;
    private String requestMessage;

    public static final int KEY_MSG_LENGTH = 0;
    public static final int KEY_REQ_TYPE = 1;
    public static final int KEY_IP_ADDRESS = 2;
    public static final int KEY_PORT_NUM = 3;


    public Node getServerNode() {
        return serverNode;
    }

    public void setServerNode(Node serverNode) {
        this.serverNode = serverNode;
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
