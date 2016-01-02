package com.vishlesha.request;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.dataType.Node;

import java.util.Date;

/**
 * Created by ridwan on 1/1/16.
 */

public class UnregisterRequest extends Request {


    public UnregisterRequest(Node node){
        super(node);
        setRequest();

    }

    void setRequest(){
        request = " UNREG " + getIpAddress() + " " + getPortNumber() + " " + GlobalConstant.getUsername();
        appendMsgLength();
    }



}
