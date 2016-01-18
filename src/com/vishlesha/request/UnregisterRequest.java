package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */

public class UnregisterRequest extends Request {


    public UnregisterRequest(Node node){
        setRecipientNode(node);
        String requestMessage = " UNREG " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber() + " " + GlobalState.getUsername();
        setRequestMessage(requestMessage);
        appendMsgLength();

    }




}
