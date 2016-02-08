package com.vishlesha.timer.task;

import com.vishlesha.app.GlobalState;
import com.vishlesha.error.handler.ErrorHandler;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.Request;

import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/20/16.
 */
public class RetryRequestTask extends TimerTask {
    Logger logger = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);
    private Request request;
    public RetryRequestTask(Request request){
       this.request = request;
    }

    private static final int MAX_RETRY_COUNT = 2;


    @Override
    public void run() {
        if (GlobalState.isResponsePending(request)){
            if (request.getRetryCount() < MAX_RETRY_COUNT){
                request.incrementRetryCount();
                Client client = new Client();
                logger.info("Resending Request: retry count " + request.getRetryCount() + " " + request.getRequestMessage() + " to " + request.getRecipientNode().toString());
                client.sendUDPRequest(request);
            }else{
                ErrorHandler errorHandler = new ErrorHandler();
                errorHandler.handleNodeUnreachable(request);
            }
        }
    }
}
