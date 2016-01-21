package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

import java.util.Date;

/**
 * Created by ridwan on 1/1/16.
 */

public class RegisterRequest extends Request {

    public RegisterRequest(Node node, String ... params){
        setRecipientNode(node);
        String userName;
        if (params.length > 0){
            userName =  params[0];
        }else{
            String currentTime = String.valueOf(new Date().getTime());
            userName = currentTime.substring(currentTime.length() - 8);
        }

        GlobalState.setUsername(userName);
        String requestMessage = " REG " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber() + " " + userName;
        setRequestMessage(requestMessage);
        appendMsgLength();
    }

    public String getHashCode(){
        return "REG-" + getRecipientNode().getIpaddress();
    }
}
