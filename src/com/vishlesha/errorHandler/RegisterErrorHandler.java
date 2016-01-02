package com.vishlesha.errorHandler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.Error;
import com.vishlesha.request.RegisterRequest;

import java.util.Random;

/**
 * Created by ridwan on 1/2/16.
 */
public class RegisterErrorHandler extends ErrorHandler {

    public RegisterErrorHandler (Error error){
        super();
        switch (error.getErrorCode()){
            case GlobalConstant.ERR_CODE_REG_USERNAME:
                setRequest(new RegisterRequest(error.getErrorNode()));
                setRetry(true);
                break;

            case GlobalConstant.ERR_CODE_REG_FULL:
                setRetry(false);
                setHandleMessage(GlobalConstant.ERR_HANDLE_REG_FULL);
                break;

            case GlobalConstant.ERR_CODE_REG_IPPORT:
                Node temp = GlobalState.getLocalServerNode();
                temp.setPortNumber(GlobalConstant.PORT_MIN + (int) (GlobalConstant.PORT_RANGE* Math.random()));
                setRequest(new RegisterRequest(temp));
                break;

            default:
                setRetry(false);
                setHandleMessage(GlobalConstant.ERR_HANDLE_REG_DEFAULT);


        }
    }
}
