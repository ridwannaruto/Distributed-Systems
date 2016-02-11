package com.vishlesha.timer.task;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.handler.ErrorHandler;
import com.vishlesha.log.AppLogger;
import com.vishlesha.message.HeartBeatMessage;
import com.vishlesha.network.Client;
import com.vishlesha.request.Request;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/20/16.
 */
public class HeartBeatTask extends TimerTask {
    private final Logger logger = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    private static final int MSG_INTERVAL = 30000; //seconds

    @Override
    public void run() {
        Client client = GlobalState.getClient();
        while (true) {
            int neighborCount = GlobalState.getNeighbors().size();

            Map<Node, List<String>> neighborList = new HashMap<>();
            neighborList.putAll(GlobalState.getNeighbors());

            for (Node node : neighborList.keySet()) {
                HeartBeatMessage heartBeatMessage = new HeartBeatMessage(neighborCount);
                heartBeatMessage.setRecipientNode(node);
                client.sendUDPMessage(heartBeatMessage);
            }
            try{
                Thread.sleep(MSG_INTERVAL);
            }catch (Exception ex){
                logger.severe("Heart Beat Message Send Failed");
            }
        }
    }
}
