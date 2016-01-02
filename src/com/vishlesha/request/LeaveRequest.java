package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class LeaveRequest extends Request {


    public LeaveRequest(Node node){
        setRecepientNode(node);
        requestMessage = " LEAVE " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber();
        appendMsgLength();
    }

    public LeaveRequest (String requestMessage){
        String[] token = requestMessage.split(" ");
        Node node = new Node();
        node.setIpaddress(token[KEY_IP_ADDRESS]);
        node.setPortNumber(Integer.valueOf(token[KEY_PORT_NUM]));
        setRecepientNode(node);
    }


}
