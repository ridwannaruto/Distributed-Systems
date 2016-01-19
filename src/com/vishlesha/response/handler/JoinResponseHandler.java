package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.response.FileListShareResponse;
import com.vishlesha.response.JoinResponse;
import jdk.nashorn.internal.objects.Global;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
public class JoinResponseHandler {

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    public void handle(JoinResponse joinResponse){

        if (!joinResponse.isFail()){
            log.info(this.getClass() + " : " + GlobalConstant.SUCCESS_MSG_JOIN);
            try{
                Node newNeighbour = joinResponse.getSenderNode();
                newNeighbour.setPortNumber(GlobalConstant.PORT_LISTEN);
                GlobalState.addNeighbor(newNeighbour);
                log.info(this.getClass() + " : new neighbour added " + newNeighbour.toString());
                Client client = new Client();
                client.sendUDPResponse(new FileListShareResponse(newNeighbour,GlobalState.getLocalFiles()));
                log.info(this.getClass() + " : local file list sent to" + newNeighbour.toString());
            }catch (IllegalStateException ex){
                //TODO
            }

        }else{
            //TODO
        }

    }


}
