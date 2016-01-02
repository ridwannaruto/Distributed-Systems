package com.vishlesha.request;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.dataType.Node;

import java.util.Date;

/**
 * Created by ridwan on 1/1/16.
 */

public class RegisterRequest extends Request {

    public RegisterRequest(Node node){
        super(node);
        setRequest();

    }

    protected void setRequest(){
        String currentTime = String.valueOf(new Date().getTime());
        String userName = currentTime.substring(currentTime.length() - 8);
        GlobalConstant.setUsername(userName);
        request = " REG " + getIpAddress() + " " + getPortNumber() + " " + userName;
        appendMsgLength();

    }


}
