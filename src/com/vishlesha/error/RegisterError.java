package com.vishlesha.error;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class RegisterError extends Error {

    private static final int ERR_CODE_COMMAND = 9998;
    private static final int ERR_CODE_USERNAME = 9999;
    private static final int ERR_CODE_IPPORT = 9997;
    private static final int ERR_CODE_BSFULL = 9996;


    public RegisterError(String errorMessage, Node errorNode) {
        super(errorNode);
        String[] token = errorMessage.split(" ");

        if (token[1].equals("ERROR")) {
            setErrorCode(GlobalConstant.ERR_CODE_GENERAL);
            setErrorMessage(GlobalConstant.ERR_MSG_GENERAL);
        } else {
            int errorCode = Integer.valueOf(token[2]);
            switch (errorCode) {
                case ERR_CODE_COMMAND:
                    setErrorCode(GlobalConstant.ERR_CODE_REG_COMMAND);
                    setErrorMessage(GlobalConstant.ERR_MSG_REG_COMMAND);
                    break;
                case ERR_CODE_USERNAME:
                    setErrorCode(GlobalConstant.ERR_CODE_REG_USERNAME);
                    setErrorMessage(GlobalConstant.ERR_MSG_REG_USERNAME);
                    break;

                case ERR_CODE_IPPORT:
                    setErrorCode(GlobalConstant.ERR_CODE_REG_IPPORT);
                    setErrorMessage(GlobalConstant.ERR_MSG_REG_IP_PORT);
                    break;

                case ERR_CODE_BSFULL:
                    setErrorCode(GlobalConstant.ERR_CODE_REG_FULL);
                    setErrorMessage(GlobalConstant.ERR_MSG_REG_BS_FULL);
                    break;

            }
        }

    }
}
