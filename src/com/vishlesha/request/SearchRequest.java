package com.vishlesha.request;

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

    public SearchRequest(String address, int port, String searchFileName, int numberOfHops){
        super(address,port);
        setFileName(searchFileName);
        setNoOfHops(numberOfHops);
        setRequest();
    }

    private void setRequest(){
        request = " SER " + getIpAddress() + " " + getPortNumber() + " " + getFileName() + " " + getNoOfHops();
        appendMsgLength();
    }
}
