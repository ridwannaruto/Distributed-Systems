package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.LeaveError;

/**
 * Created by ridwan on 1/1/16.
 */
public class LeaveResponse extends Response {

    int responseCode;

    public LeaveResponse(String responseMessage, Node respondNode){
        setRespondNode(respondNode);
        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("LEAVEOK") && responseCode == 0){
            setFail(false);
            if (!GlobalState.isTestMode())
                System.out.println(GlobalConstant.SUCCESS_MSG_LEAVE);
        }

        else{
            setFail(true);
            LeaveError leaveError = new LeaveError(responseMessage,respondNode);
            if (!GlobalState.isTestMode())
                System.out.println("Leave Error: " + leaveError.getErrorMessage());
        }

    }

    public LeaveResponse(int responseCode){
        String responseMessage = " LEAVEOK " + responseCode;
        setResponseMessage(responseMessage);
        appendMsgLength();
    }
}
