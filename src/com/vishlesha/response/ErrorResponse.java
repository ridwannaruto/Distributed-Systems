package com.vishlesha.response;

/**
 * Created by ridwan on 1/3/16.
 */
public class ErrorResponse extends Response{
    public ErrorResponse(){
        setResponseMessage(" ERROR");
        appendMsgLength();
    }
}
