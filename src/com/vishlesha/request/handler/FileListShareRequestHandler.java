package com.vishlesha.request.handler;

import com.vishlesha.app.GlobalState;
import com.vishlesha.request.FileListShareRequest;
import com.vishlesha.response.FileListShareResponse;

/**
 * Created by ridwan on 1/2/16.
 */
public class FileListShareRequestHandler {

    private static final int RESPOND_CODE_SEARCH_UNREACHABLE = 9999;
    private static final int RESPOND_CODE_SEARCH_ERROR = 9998;

    FileListShareResponse response;

    public FileListShareRequestHandler(FileListShareRequest request){
        GlobalState.addNeighborFiles(request.getRecepientNode(), request.getFiles());
        setResponse(new FileListShareResponse(GlobalState.getLocalServerNode(), GlobalState.getLocalFiles()));
    }

    public FileListShareResponse getResponse() {
        return response;
    }

    public void setResponse(FileListShareResponse response) {
        this.response = response;
    }
}
