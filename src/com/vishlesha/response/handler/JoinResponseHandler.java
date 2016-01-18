package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.network.Client;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.response.FileListShareResponse;
import com.vishlesha.response.JoinResponse;

/**
 * Created by ridwan on 1/18/16.
 */
public class JoinResponseHandler {

    public void handle(JoinResponse joinResponse){

        if (!joinResponse.isFail()){
            try{
                Node newNeighbour = joinResponse.getSenderNode();
                newNeighbour.setPortNumber(GlobalConstant.PORT_LISTEN);
                GlobalState.addNeighbor(newNeighbour);
                Client client = new Client();
                client.sendUDPResponse(new FileListShareResponse(newNeighbour,GlobalState.getLocalFiles()));

            }catch (IllegalStateException ex){
                //TODO
            }

        }else{
            //TODO
        }

    }


}
