package com.vishlesha.response;

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
            if (!globalConstant.isTestMode())
                System.out.println(globalConstant.SUCCESS_MSG_UNREG);
        }

        else{
            setError(true);
            UnregisterError unregisterError = new UnregisterError(responseMessage);
            if (!globalConstant.isTestMode())
                System.out.println("Unregister Error: " + unregisterError.getErrorMessage());
        }

    }

}
