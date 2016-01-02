package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

import java.util.Date;

/**
 * Created by ridwan on 1/1/16.
 */

public class RegisterRequest extends Request {

    public RegisterRequest(Node node, String ... params){
        super(node);
        setRequest(params);

    }

    protected void setRequest(String ... params){
        String userName;
        if (params.length > 0){
            userName =  params[0];
        }else{
            String currentTime = String.valueOf(new Date().getTime());
            userName = currentTime.substring(currentTime.length() - 8);
        }

        GlobalState.setUsername(userName);
        requestMessage = " REG " + GlobalState.getLocalServerNode().getIpaddress() + " " + GlobalState.getLocalServerNode().getPortNumber() + " " + userName;
        appendMsgLength();

    }


}
