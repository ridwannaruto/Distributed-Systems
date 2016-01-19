package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.errorHandler.RegisterErrorHandler;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.response.LeaveResponse;
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
            log.info(this.getClass() + " : " + GlobalConstant.SUCCESS_MSG_REG);
            ArrayList<Node> neighbour = registerResponse.getNodeList();
            for (int i = 0; i < neighbour.size(); i++) {
                GlobalState.addNeighbor(neighbour.get(i));
            }
            int j, prev = -1;
            int l = neighbour.size();
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
                JoinRequest jr = new JoinRequest(neighbour.get(rand1));
                client.sendUDPRequest(jr);
            }
            if (j == 0) {
                log.info(this.getClass() + " : This is the first node");
                System.out.println("First Node --> No joins ");
            }

        }else{
            RegisterErrorHandler registerErrorHandler = new RegisterErrorHandler();
            registerErrorHandler.handle(registerResponse.getError());
        }

    }
}
