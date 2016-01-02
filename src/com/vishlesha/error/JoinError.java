package com.vishlesha.error;

import com.vishlesha.app.GlobalConstant;

/**
 * Created by ridwan on 1/1/16.
 */
public class JoinError extends Error{

    public JoinError (String errorMessage){
        String[] token = errorMessage.split(" ");

        if (token[1].equals("ERROR")){
            setErrorCode(0);
            setErrorMessage(GlobalConstant.ERR_MSG_GENERAL);
        }else{
            int errorCode = Integer.valueOf(token[2]);
            switch (errorCode){
                case 9999:
                    setErrorCode(GlobalConstant.ERR_CODE_JOIN);
                    setErrorMessage(GlobalConstant.ERR_MSG_JOIN);
                    break;
            }
        }
    }
}
