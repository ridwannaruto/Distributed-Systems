package com.vishlesha.request;

/**
 * Created by ridwan on 1/1/16.
 */
public class LeaveRequest extends Request {


    public LeaveRequest(String address, int port){
        super(address,port);
        setRequest();
    }

    void setRequest(){
        request = " LEAVE " + getIpAddress() + " " + getPortNumber();
        appendMsgLength();
    }


}
