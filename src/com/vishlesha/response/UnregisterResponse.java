package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.UnregisterError;

/**
 * Created by ridwan on 1/1/16.
 */

public class UnregisterResponse extends Response {

    int responseCode;

    public UnregisterResponse(String responseMessage, Node respondNode){
        super(respondNode);
        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("UNROK") && responseCode == 0){
            setFail(false);
            if (!GlobalState.isTestMode())
                System.out.println(GlobalConstant.SUCCESS_MSG_UNREG);
        }

        else{
            setFail(true);
            UnregisterError unregisterError = new UnregisterError(responseMessage,respondNode);
            if (!GlobalState.isTestMode())
                System.out.println("Unregister Error: " + unregisterError.getErrorMessage());
        }

    }

}
