package com.vishlesha.error;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class Error {
    private Node errorNode;
    private int errorCode;
    private String errorMessage;

    Error(Node errorNode) {
        setErrorNode(errorNode);
    }

    public Node getErrorNode() {
        return errorNode;
    }

    void setErrorNode(Node node) {
        this.errorNode = node;
    }

    public int getErrorCode() {
        return errorCode;
    }

    void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
