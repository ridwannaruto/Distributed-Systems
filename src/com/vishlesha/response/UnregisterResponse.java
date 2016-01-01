package com.vishlesha.response;

import com.vishlesha.dataType.Node;

import java.util.ArrayList;

/**
 * Created by ridwan on 1/1/16.
 */

public class UnregisterResponse extends Response {

    int responseCode;

    public UnregisterResponse(String responseMessage){

        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("UNROK") && responseCode == 0)
            System.out.println(constant.UNREGISTER_SUCCESS_RESPONSE);
        else
            System.out.println(constant.UNREGISTER_ERROR_RESPONSE);
    }

}
