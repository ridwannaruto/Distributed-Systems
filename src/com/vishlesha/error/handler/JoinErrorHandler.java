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
class JoinErrorHandler {

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);


    public void handleNodeUnreachable(Request request) {
        Node unreachableNode = request.getRecipientNode();
        Client client = GlobalState.getClient();
        try {
            int availableNodeCount = GlobalState.getRegisteredNodeCount();
            if (availableNodeCount > 0) {
                GlobalState.removeRegisteredNode(unreachableNode);
                availableNodeCount = GlobalState.getRegisteredNodeCount();

                if (availableNodeCount > 0) {
                    final Random rand = new Random();
                    int randIndex = rand.nextInt(availableNodeCount);
                    Node newJoinNode = GlobalState.getRegisteredNode(randIndex);
                    JoinRequest jr = new JoinRequest(newJoinNode);
                    client.sendUDPRequest(jr, true);
                    log.info("New join request sent to " + newJoinNode.toString());
                } else {
                    log.warning("No other reachable nodes to join");
                }
            }

        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }

    }
}
