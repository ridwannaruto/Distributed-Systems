package com.vishlesha.network;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/2/16.
 */
abstract public class CallBack {

    public static final CallBack emptyCallback = new CallBack() {
        @Override
        public void run(String message, Node node) {}
    };

    abstract public void run(String message, Node node);
}
