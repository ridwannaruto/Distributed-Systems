package com.vishlesha.request;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class LeaveRequest extends Request {


    public LeaveRequest(Node node){
        super(node);
        setRequest();
    }

    void setRequest(){
        request = " LEAVE " + getIpAddress() + " " + getPortNumber();
        appendMsgLength();
    }


}
