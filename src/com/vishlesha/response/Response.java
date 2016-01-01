package com.vishlesha.response;

import com.vishlesha.Constant;

/**
 * Created by ridwan on 1/1/16.
 */

abstract public class Response {
    protected String response;
    protected Constant constant;

    public Response(){
        constant = new Constant();
    }

    public String getResponse(){
        return response;
    }

}
