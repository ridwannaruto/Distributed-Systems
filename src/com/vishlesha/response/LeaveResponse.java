package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.error.JoinError;
import com.vishlesha.error.LeaveError;

/**
 * Created by ridwan on 1/1/16.
 */
public class LeaveResponse extends Response {

    int responseCode;

    public LeaveResponse(String responseMessage){

        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("LEAVEOK") && responseCode == 0){
            setError(false);
            if (!GlobalConstant.isTestMode())
                System.out.println(GlobalConstant.SUCCESS_MSG_LEAVE);
        }

        else{
            setError(true);
            LeaveError leaveError = new LeaveError(responseMessage);
            if (!GlobalConstant.isTestMode())
                System.out.println("Leave Error: " + leaveError.getErrorMessage());
        }

    }

}
