package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.LeaveResponse;

/**
 * Created by ridwan on 1/18/16.
 */
public class LeaveResponseHandler {

    public void handle(LeaveResponse leaveResponse){

        if (!leaveResponse.isFail()){
            try{
                Node oldNeighbour = leaveResponse.getSenderNode();
                oldNeighbour.setPortNumber(GlobalConstant.PORT_LISTEN);
                GlobalState.removeNeighbor(oldNeighbour);

            }catch (IllegalStateException ex){
                //TODO
            }

        }else{
            //TODO
        }

    }

}
