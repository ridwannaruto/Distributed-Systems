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

    public void handle(JoinResponse joinResponse) {

        try{
            String key = "JOIN-" + joinResponse.getRecipientNode().getIpaddress();
            GlobalState.removeResponsePendingRequest(key);
            log.info("removed request from pending list");
        }catch (Exception ex){
            log.warning("could not remove request from pending list");
        }

        try {

            Node newNeighbour = joinResponse.getSenderNode();
            newNeighbour.setPortNumber(GlobalConstant.PORT_LISTEN);
            GlobalState.addNeighbor(newNeighbour);
            log.info("new neighbour added " + newNeighbour.toString());
            Client client = new Client();
            client.sendUDPResponse(new FileListShareResponse(newNeighbour, GlobalState.getLocalFiles()));
            log.info("local file list sent to" + newNeighbour.toString());
        } catch (IllegalStateException ex) {
            //TODO
            log.warning("node already exists");
        }


    }


}
