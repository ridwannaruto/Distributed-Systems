package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.JoinError;

/**
 * Created by ridwan on 1/1/16.
 */
public class JoinResponse extends Response{
    int responseCode;

    public JoinResponse(String responseMessage, Node respondNode){
        setRespondNode(respondNode);
        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("JOINOK") && responseCode == 0){
            setFail(false);
            if (!GlobalState.isTestMode())
                System.out.println(GlobalConstant.SUCCESS_MSG_JOIN);
        }

        else{
            setFail(true);
            JoinError joinError = new JoinError(responseMessage,respondNode);
            if (!GlobalState.isTestMode())
                System.out.println("Join Error: " + joinError.getErrorMessage());
        }

    }

    public JoinResponse(int responseCode){
        String responseMessage = " JOINOK " + responseCode;
        setResponseMessage(responseMessage);
        appendMsgLength();
    }


}
