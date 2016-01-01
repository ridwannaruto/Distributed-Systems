package com.vishlesha.request;

import java.util.Date;

/**
 * Created by ridwan on 1/1/16.
 */

public class UnregisterRequest extends Request {


    public UnregisterRequest(String address, int port){
        super(address,port);
        setRequest();

    }

    void setRequest(){
        String currentTime = String.valueOf(new Date().getTime());
        String userName = currentTime.substring(currentTime.length() - 8);
        request = " UNREG " + getIpAddress() + " " + getPortNumber() + " " + userName;
        appendMsgLength();
    }



}
