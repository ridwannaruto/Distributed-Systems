package com.vishlesha.timer.task;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.handler.ErrorHandler;
import com.vishlesha.log.AppLogger;
import com.vishlesha.message.HeartBeatMessage;
import com.vishlesha.network.Client;
import com.vishlesha.request.Request;

import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/20/16.
 */
public class HeartBeatTask extends TimerTask {
    private final Logger logger = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    private static final int MSG_INTERVAL = 30000; //seconds

    @Override
    public void run() {
        Client client = new Client();
        while (true) {
            int neighborCount = GlobalState.getNeighbors().size();
            HeartBeatMessage heartBeatMessage = new HeartBeatMessage(neighborCount);

            Iterator iterator = GlobalState.getNeighbors().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry)iterator.next();
                heartBeatMessage.setRecipientNode((Node)pair.getKey());
                client.sendUDPMessage(heartBeatMessage);
                iterator.remove(); // avoids a ConcurrentModificationException
            }

            try{
                Thread.sleep(MSG_INTERVAL);
            }catch (Exception ex){
                logger.severe("Heart Beat Message Send Failed");
            }
        }
    }
}
