package com.vishlesha.error;

import com.vishlesha.app.GlobalConstant;

/**
 * Created by ridwan on 1/1/16.
 */
public class RegisterError extends Error {

    public RegisterError(String errorMessage){
        String[] token = errorMessage.split(" ");

        if (token[1].equals("ERROR")){
            setErrorCode(GlobalConstant.ERR_CODE_GENERAL);
            setErrorMessage(GlobalConstant.ERR_MSG_GENERAL);
        }else{
            int errorCode = Integer.valueOf(token[2]);
            switch (errorCode){
                case 9999:
                    setErrorCode(GlobalConstant.ERR_CODE_REG_COMMAND);
                    setErrorMessage(GlobalConstant.ERR_MSG_REG_COMMAND);
                    break;
                case 9998:
                    setErrorCode(GlobalConstant.ERR_CODE_REG_USERNAME);
                    setErrorMessage(GlobalConstant.ERR_MSG_REG_USERNAME);
                    break;

                case 9997:
                    setErrorCode(GlobalConstant.ERR_CODE_REG_IPPORT);
                    setErrorMessage(GlobalConstant.ERR_MSG_REG_IP_PORT);
                    break;

                case 9996:
                    setErrorCode(GlobalConstant.ERR_CODE_REG_FULL);
                    setErrorMessage(GlobalConstant.ERR_MSG_REG_BS_FULL);
                    break;

            }
        }

    }
}
