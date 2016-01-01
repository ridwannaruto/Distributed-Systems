package com.vishlesha.error;

/**
 * Created by ridwan on 1/1/16.
 */
public class RegisterError extends Error {

    public RegisterError(String errorMessage){
        String[] token = errorMessage.split(" ");

        if (token[1].equals("ERROR")){
            setErrorCode(globalConstant.ERR_CODE_GENERAL);
            setErrorMessage(globalConstant.ERR_MSG_GENERAL);
        }else{
            int errorCode = Integer.valueOf(token[2]);
            switch (errorCode){
                case 9999:
                    setErrorCode(globalConstant.ERR_CODE_REG_COMMAND);
                    setErrorMessage(globalConstant.ERR_MSG_REG_COMMAND);
                    break;
                case 9998:
                    setErrorCode(globalConstant.ERR_CODE_REG_USERNAME);
                    setErrorMessage(globalConstant.ERR_MSG_REG_USERNAME);
                    break;

                case 9997:
                    setErrorCode(globalConstant.ERR_CODE_REG_IPPORT);
                    setErrorMessage(globalConstant.ERR_MSG_REG_IP_PORT);
                    break;

                case 9996:
                    setErrorCode(globalConstant.ERR_CODE_REG_FULL);
                    setErrorMessage(globalConstant.ERR_MSG_REG_BS_FULL);
                    break;

            }
        }

    }
}
