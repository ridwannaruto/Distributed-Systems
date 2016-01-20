package com.vishlesha.request;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class JoinRequest extends Request {


    public JoinRequest(Node recipientNode){
        setRecipientNode(recipientNode);
        setInitialNode(GlobalState.getLocalServerNode());
        String requestMessage = " JOIN " + getInitialNode().getIpaddress() + " " + getInitialNode().getPortNumber();
        setRequestMessage(requestMessage);
        appendMsgLength();
        setHashCode("JOIN-" + getRecipientNode().getIpaddress());
    }

    public JoinRequest (String requestMessage){
        String[] token = requestMessage.split(" ");
        Node node = new Node();
        node.setIpaddress(token[KEY_IP_ADDRESS]);
        node.setPortNumber(Integer.valueOf(token[KEY_PORT_NUM]));
        setInitialNode(node);
    }



}
