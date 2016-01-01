package com.vishlesha.request;

/**
 * Created by ridwan on 1/1/16.
 */
public class JoinRequest extends Request {


    public JoinRequest(String address, int port){
        super(address,port);
        setRequest();
    }

    void setRequest(){
        request = " JOIN " + getIpAddress() + " " + getPortNumber();
        appendMsgLength();
    }


}
