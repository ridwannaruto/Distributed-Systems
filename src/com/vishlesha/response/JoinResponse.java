package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.error.JoinError;
import com.vishlesha.error.UnregisterError;

/**
 * Created by ridwan on 1/1/16.
 */
public class JoinResponse extends Response{
    int responseCode;

    public JoinResponse(String responseMessage){

        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("JOINOK") && responseCode == 0){
            setError(false);
            if (!GlobalConstant.isTestMode())
                System.out.println(GlobalConstant.SUCCESS_MSG_JOIN);
        }

        else{
            setError(true);
            JoinError joinError = new JoinError(responseMessage);
            if (!GlobalConstant.isTestMode())
                System.out.println("Join Error: " + joinError.getErrorMessage());
        }

    }
}
