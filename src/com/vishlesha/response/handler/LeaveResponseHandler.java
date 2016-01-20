package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.LeaveResponse;

import java.util.logging.Logger;

/**
 * Created by ridwan on 1/18/16.
 */
public class LeaveResponseHandler {

    Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
    public void handle(LeaveResponse leaveResponse){

        if (!leaveResponse.isFail()){
            log.info(this.getClass() + " : " + GlobalConstant.SUCCESS_MSG_LEAVE);
            try{
                Node oldNeighbour = leaveResponse.getSenderNode();
                oldNeighbour.setPortNumber(GlobalConstant.PORT_LISTEN);
                GlobalState.removeNeighbor(oldNeighbour);
                log.info(this.getClass() + " : removed neighbour " + oldNeighbour.toString() );

            }catch (IllegalStateException ex){
                //TODO
                log.warning(this.getClass() + " : node doesn't exists");
            }

        }else{
            //TODO
        }

    }

}
