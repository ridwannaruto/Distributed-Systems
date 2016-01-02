package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.error.UnregisterError;

/**
 * Created by ridwan on 1/1/16.
 */

public class UnregisterResponse extends Response {

    int responseCode;

    public UnregisterResponse(String responseMessage){

        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("UNROK") && responseCode == 0){
            setError(false);
            if (!GlobalConstant.isTestMode())
                System.out.println(GlobalConstant.SUCCESS_MSG_UNREG);
        }

        else{
            setError(true);
            UnregisterError unregisterError = new UnregisterError(responseMessage);
            if (!GlobalConstant.isTestMode())
                System.out.println("Unregister Error: " + unregisterError.getErrorMessage());
        }

    }

}
