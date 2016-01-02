package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNoOfHops() {
        return noOfHops;
    }

    public void setNoOfHops(int noOfHops) {
        this.noOfHops = noOfHops;
    }

    public SearchRequest(Node node, String searchFileName, int numberOfHops){
        super(node);
        setFileName(searchFileName);
        setNoOfHops(numberOfHops);
        setRequest();
    }

    private void setRequest(){
        requestMessage = " SER " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber() + " " + getFileName() + " " + getNoOfHops();
        appendMsgLength();
    }
}
