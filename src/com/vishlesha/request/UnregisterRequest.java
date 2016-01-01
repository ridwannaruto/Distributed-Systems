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
        request = " UNREG " + getIpAddress() + " " + getPortNumber() + " " + globalConstant.getUsername();
        appendMsgLength();
    }



}
