package com.vishlesha.error.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.error.Error;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.RegisterRequest;
import com.vishlesha.request.Request;
import com.vishlesha.request.UnregisterRequest;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/2/16.
 */
class RegisterErrorHandler extends ErrorHandler {

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    public void handleErrorResponse(Error error) {
        Client client = GlobalState.getClient();
        switch (error.getErrorCode()) {
            case GlobalConstant.ERR_CODE_REG_USERNAME:
                log.severe("Node register failed: duplicate username");
                Request newRequest = new RegisterRequest(error.getErrorNode());
                client.sendTCPRequest(newRequest, true);
                log.info("Resending register request to Bootstrap Node");
                break;

            case GlobalConstant.ERR_CODE_REG_FULL:
                log.severe("Node register failed: registration full");
                System.out.println(GlobalConstant.ERR_HANDLE_REG_FULL);
                break;

            case GlobalConstant.ERR_CODE_REG_IPPORT:
                log.severe("Node register failed:ip and port already registered");
                Request unregReq = new UnregisterRequest(error.getErrorNode());
                client.sendTCPRequest(unregReq, false);
                Request regReq = new RegisterRequest(error.getErrorNode());
                client.sendTCPRequest(regReq, true);
                break;

        }
    }
}
