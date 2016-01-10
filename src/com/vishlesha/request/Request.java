package com.vishlesha.request;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class Request {
    protected Node recepientNode;
    protected String requestMessage;

    public static final int KEY_MSG_LENGTH = 0;
    public static final int KEY_REQ_TYPE = 1;
    public static final int KEY_IP_ADDRESS = 2;
    public static final int KEY_PORT_NUM = 3;

   private Node sender;

   public Node getSender() {
      return sender;
   }

   public void setSender(Node sender) {
      this.sender = sender;
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

   public void setRequestMessage(String requestMessage) {
      this.requestMessage = requestMessage;
   }

   protected void appendMsgLength(){
        requestMessage = String.format("%04d", requestMessage.length()+4) + requestMessage;
    }


}
