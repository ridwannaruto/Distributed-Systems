package com.vishlesha.response.handler;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.network.Client;
import com.vishlesha.response.FileListShareResponse;

/**
 * Created by ridwan on 1/18/16.
 */
public class FileShareResponseHandler {

    public void handle(FileListShareResponse fileListShareResponse){
        Node neighbour = fileListShareResponse.getRecipientNode();
        neighbour.setPortNumber(GlobalConstant.PORT_LISTEN);
        GlobalState.addNeighborFiles(neighbour, fileListShareResponse.getFiles());
        Client client = new Client();
        client.sendUDPResponse(new FileListShareResponse(neighbour,GlobalState.getLocalFiles()));
    }

}
