package com.vishlesha.request;

import java.util.Date;

/**
 * Created by ridwan on 1/1/16.
 */

public class RegisterRequest extends Request {

    public RegisterRequest(String address, int port){
        super(address,port);
        setRequest();

    }

    protected void setRequest(){
        String currentTime = String.valueOf(new Date().getTime());
        String userName = currentTime.substring(currentTime.length() - 8);
        request = " REG " + getIpAddress() + " " + getPortNumber() + " " + userName;
        appendMsgLength();

    }


}
