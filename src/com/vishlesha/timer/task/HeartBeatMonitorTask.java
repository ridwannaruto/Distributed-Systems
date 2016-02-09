package com.vishlesha.timer.task;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.message.HeartBeatMessage;
import com.vishlesha.network.Client;
import com.vishlesha.request.RegisterRequest;
import com.vishlesha.request.Request;
import com.vishlesha.request.UnregisterRequest;

import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/20/16.
 */
public class HeartBeatMonitorTask extends TimerTask {
    private final Logger logger = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    private static final int MONITOR_INTERVAL = 60000; //seconds

    Client client = new Client();


    @Override
    public void run() {
        while (true) {

            GlobalState.acquireHeartBeatMonitorLock();

            for (Node node : GlobalState.getNeighbors().keySet()) {
                Integer count = GlobalState.getNeighborCountList().get(node);
                if (count > 0){
                    GlobalState.updateNeighborCount(node,-1);
                }else if (count == -1){
                    GlobalState.removeNeighbor(node);
                    logger.info("Unreachable node removed " + node.toString());
                }
            }

            if(GlobalState.getNeighbors().isEmpty()){
                Request unregReq = new UnregisterRequest(GlobalState.getBootstrapNode());
                client.sendTCPRequest(unregReq);
                Request regReq = new RegisterRequest(GlobalState.getBootstrapNode());
                client.sendTCPRequest(regReq);
                logger.info("Re-registered to the network");
                GlobalState.acquireHeartBeatMonitorLock();
            }

            try{
                Thread.sleep(MONITOR_INTERVAL);
            }catch (Exception ex){
                logger.severe("Heart Beat Monitor Failed");
            }

            GlobalState.releaseHeartBeatMonitorLock();
        }
    }
}
