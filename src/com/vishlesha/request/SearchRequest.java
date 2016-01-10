package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class SearchRequest extends Request {

    private String fileName;
    private int noOfHops;
    private Node initiator;

   public Node getInitiator() {
      return initiator;
   }

   public void setInitiator(Node initiator) {
      this.initiator = initiator;
   }

   public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNoOfHops() {
        return noOfHops;
    }

    public void setNoOfHops(int noOfHops) {
        this.noOfHops = noOfHops;
    }

    public SearchRequest(Node initiator, String searchFileName, int numberOfHops){
        setRecepientNode(initiator);
        setFileName(searchFileName);
        setNoOfHops(numberOfHops);
        requestMessage = " SER " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber() + " " + getFileName() + " " + getNoOfHops();
        appendMsgLength();
    }

    public SearchRequest (String requestMessage){
        String[] token = requestMessage.split(" ");
        Node node = new Node();
        node.setIpaddress(token[KEY_IP_ADDRESS]);
        node.setPortNumber(Integer.valueOf(token[KEY_PORT_NUM]));
        setRecepientNode(node);
    }

}
