package com.vishlesha.response;

import com.vishlesha.dataType.Node;
import com.vishlesha.error.*;
import com.vishlesha.error.Error;

/**
 * Created by ridwan on 1/1/16.
 */

abstract public class Response {
    protected Node respondNode;
    protected String response;
    protected boolean fail;
    protected Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Response(Node node){
        setRespondNode(node);
    }

    public Node getRespondNode() {
        return respondNode;
    }

    public void setRespondNode(Node respondNode) {
        this.respondNode = respondNode;
    }

    public String getResponse(){
        return response;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

}
