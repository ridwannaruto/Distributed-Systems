package com.vishlesha.error;
import com.vishlesha.app.GlobalConstant;

/**
 * Created by ridwan on 1/1/16.
 */
public class Error {

    protected int errorCode;
    protected String errorMessage;


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
