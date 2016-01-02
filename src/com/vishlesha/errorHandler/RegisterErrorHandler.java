package com.vishlesha.errorHandler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.error.RegisterError;

/**
 * Created by ridwan on 1/2/16.
 */
public class RegisterErrorHandler extends ErrorHandler {

    public RegisterErrorHandler (RegisterError error){
        super();
        switch (error.getErrorCode()){
            case GlobalConstant.ERR_CODE_REG_USERNAME:


        }
    }
}
