package com.vishlesha.request;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class SearchRequest extends Request {

    private String fileName;
    private int noOfHops;

    public String getFileName() {
        return fileName;
    }

    void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNoOfHops() {
        return noOfHops;
    }

    public void setNoOfHops(int noOfHops) {
        this.noOfHops = noOfHops;
    }

    public SearchRequest(Node initiator, Node recipient, String searchFileName, int numberOfHops) {
        setRecipientNode(recipient);
        setFileName(searchFileName);
        setNoOfHops(numberOfHops);
        setInitialNode(initiator);
        String requestMessage = " SER " + getInitialNode().getIpaddress() + " " + getInitialNode().getPortNumber() + " " + getFileName() + " " + getNoOfHops();
        setRequestMessage(requestMessage);
        appendMsgLength();
    }

    public SearchRequest(String requestMessage) {
        String[] token = requestMessage.split(" ");

        Node node = new Node();
        node.setIpaddress(token[KEY_IP_ADDRESS]);
        node.setPortNumber(Integer.valueOf(token[KEY_PORT_NUM]));

        setInitialNode(node);
        setNoOfHops(Integer.valueOf(token[5]));
        setFileName(token[4]);
        setRequestMessage(requestMessage);
    }

    public void updateRequestMessage() {
        String requestMessage = " SER " + getInitialNode().getIpaddress() + " " + getInitialNode().getPortNumber() + " " + getFileName() + " " + getNoOfHops();
        setRequestMessage(requestMessage);
        appendMsgLength();
    }

    public String getHashCode() {
        return "SER-" + getRecipientNode().getIpaddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchRequest that = (SearchRequest) o;

        if (!fileName.equals(that.fileName)) return false;
        if (getInitialNode() != null ? !getInitialNode().equals(that.getInitialNode()) : that.getInitialNode() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + (getInitialNode() != null ? getInitialNode().hashCode() : 0);
        return result;
    }
}
