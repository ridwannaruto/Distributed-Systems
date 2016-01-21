package com.vishlesha.error.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.Error;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.RegisterRequest;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/2/16.
 */
public class RegisterErrorHandler extends ErrorHandler {

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    public void handle (Error error){
        Client client = new Client();
        switch (error.getErrorCode()){
            case GlobalConstant.ERR_CODE_REG_USERNAME:
                log.severe(this.getClass() + " : duplicate username");
                setRequest(new RegisterRequest(error.getErrorNode()));
                client.sendTCPRequest(getRequest());
                break;

            case GlobalConstant.ERR_CODE_REG_FULL:
                log.info(this.getClass() + " : registration full");
                setHandleMessage(GlobalConstant.ERR_HANDLE_REG_FULL);
                break;

            case GlobalConstant.ERR_CODE_REG_IPPORT:
                log.info(this.getClass() + " : ip and port already registered");
                Node temp = GlobalState.getLocalServerNode();
                temp.setPortNumber(GlobalConstant.PORT_MIN + (int) (GlobalConstant.PORT_RANGE* Math.random()));
                setRequest(new RegisterRequest(temp));
                break;

            default:
                setRetry(false);
                setHandleMessage(GlobalConstant.ERR_HANDLE_REG_DEFAULT);


        }
    }
}
