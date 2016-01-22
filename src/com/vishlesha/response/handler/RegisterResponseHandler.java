package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.RegisterError;
import com.vishlesha.error.handler.ErrorHandler;
import com.vishlesha.error.handler.RegisterErrorHandler;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.response.RegisterResponse;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
public class RegisterResponseHandler {

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    public void handle(RegisterResponse registerResponse){
        final Random rand = new Random();
        Client client = new Client();
        if (!registerResponse.isFail()){
            log.info(GlobalConstant.SUCCESS_MSG_REG);
            ArrayList<Node> registeredList = registerResponse.getNodeList();
            GlobalState.setRegisteredNodeList(registeredList);
            int j, prev = -1;
            int l = registeredList.size();

            j=l;
            for (int i=0; i<2;i++){
                int node = GlobalConstant.topology[l+1][i];
                if (node != 0){
                    JoinRequest jr = new JoinRequest(registeredList.get(node-1));
                    client.sendUDPRequest(jr);
                }
            }
            /*
            for (j = 0; j < 2 && j < l; j++) {
                int rand1;
                if (l < 3) {
                    rand1 = j;
                } else {
                    // pick a previously unused random number
                    do {
                        rand1 = rand.nextInt(l);
                    } while (prev == rand1);
                }
                prev = rand1;
                //System.out.println("Rand : " + rand1);
                JoinRequest jr = new JoinRequest(registeredList.get(rand1));
                client.sendUDPRequest(jr);
            }
            */

            if (j == 0) {
                log.info("This is the first node");
                System.out.println("waiting for other nodes to connect..... ");
            }

        }else{
            RegisterError registerError = new RegisterError(registerResponse.getResponseMessage(),registerResponse.getRecipientNode());
            ErrorHandler errorHandler = new ErrorHandler();
            errorHandler.handleError(registerError);
        }

    }
}
