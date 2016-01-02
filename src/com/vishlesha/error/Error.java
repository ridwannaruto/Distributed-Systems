package com.vishlesha.error;
import com.vishlesha.app.GlobalConstant;
import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class Error {
    protected Node errorNode;
    protected int errorCode;
    protected String errorMessage;

    public Error (Node errorNode){
        setErrorNode(errorNode);
    }

    public Node getErrorNode() {
        return errorNode;
    }

    public void setErrorNode(Node node) {
        this.errorNode = node;
    }

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
