package com.vishlesha.request;

/**
 * Created by ridwan on 1/1/16.
 */
public class Request {
    private String ipAddress;
    private int portNumber;
    protected String request;

    public Request(String address, int port){
        setIpAddress(address);
        setPortNumber(port);
    }
    public int getPortNumber() {
        return portNumber;
    }

    protected void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    protected void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRequest(){
        return request;
    }

    protected void appendMsgLength(){
        request = String.format("%04d",request.length()+4) +  request;
    }


}
