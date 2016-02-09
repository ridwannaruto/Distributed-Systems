package com.vishlesha.timer.task;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.message.HeartBeatMessage;
import com.vishlesha.network.Client;

import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/20/16.
 */
public class HeartBeatMonitorTask extends TimerTask {
    private final Logger logger = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    private static final int MONITOR_INTERVAL = 60000; //seconds

    @Override
    public void run() {
        while (true) {
            for (Node node : GlobalState.getNeighbors().keySet()) {
                Integer count = GlobalState.getNeighborCountList().get(node);
                if (count > 0){
                    GlobalState.updateNeighborCount(node,-1);
                }else if (count == -1){
                    GlobalState.removeNeighbor(node);
                    logger.info("Unreachable node removed " + node.toString());
                }
            }
            try{
                Thread.sleep(MONITOR_INTERVAL);
            }catch (Exception ex){
                logger.severe("Heart Beat Monitor Failed");
            }
        }
    }
}
