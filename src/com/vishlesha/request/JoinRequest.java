package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class JoinRequest extends Request {


    public JoinRequest(Node node){
        super(node);
        setRequest();
    }

    void setRequest(){
        requestMessage = " JOIN " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber();
        appendMsgLength();
    }


}
