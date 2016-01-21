package com.vishlesha.error.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.Request;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/21/16.
 */
public class LeaveErrorHandler {

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);


    public void handleNodeUnreachable(Request request){
        Node unreachableNode = request.getRecipientNode();
        Client client = new Client();
        try{
            int availableNodeCount = GlobalState.getRegisteredNodeCount();
            if (availableNodeCount > 0){
                GlobalState.removeRegisteredNode(unreachableNode);
                final Random rand = new Random();
                int randIndex = rand.nextInt();
                Node newJoinNode = GlobalState.getRegisteredNode(randIndex);
                JoinRequest jr = new JoinRequest(newJoinNode);
                client.sendUDPRequest(jr);
            }

        }catch (Exception ex){
            log.severe(ex.getMessage());
        }

    }
}
