package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.JoinError;
import com.vishlesha.log.AppLogger;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/1/16.
 */
public class JoinResponse extends Response{
    int responseCode;
    Logger logger = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);
    public JoinResponse(String responseMessage, Node senderNode){
        setSenderNode(senderNode);
        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("JOINOK") && responseCode == 0){
            setFail(false);
        }

        else{
            setFail(true);
            JoinError joinError = new JoinError(responseMessage,senderNode);
            logger.warning("could not join node " + senderNode.getIpaddress());
        }

    }

    public JoinResponse(int responseCode){
        String responseMessage = " JOINOK " + responseCode;
        setResponseMessage(responseMessage);
        appendMsgLength();
    }



}
