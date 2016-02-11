package com.vishlesha.response;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.SearchError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ridwan on 1/1/16.
 */
public class SearchResponse extends Response {

    private int responseCode;
    private final List<String> fileList = new ArrayList<>();
    private int noOfHops;


    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    // decoding response sent from another node
    public SearchResponse(String responseMessage, Node senderNode) {
        super(responseMessage, senderNode);

        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);
        setNoOfHops(Integer.valueOf(token[5]));

        if (token[1].equals("SEROK") && responseCode == 0) {

        } else if (token[1].equals("SEROK") && responseCode < 9000) {
            fileList.addAll(Arrays.asList(token).subList(6, token.length));
        } else {
            setFail(true);
            SearchError searchError = new SearchError(responseMessage, senderNode);
        }
    }

    public int getNoOfHops() {
        return noOfHops;
    }

    void setNoOfHops(int noOfHops) {
        this.noOfHops = noOfHops;
    }

    public List<String> getFileList() {
        return fileList;
    }

    // responding with files on local node
    public SearchResponse(int responseCode, int noOfHops, List<String> fileList) {
        String fileNameList = "";
        for (String aFileList : fileList) {
            fileNameList += " " + aFileList;
        }
        String responseMessage = " SEROK " + responseCode + " " + GlobalState.getLocalServerNode().getIpaddress() +
                " " + GlobalState.getLocalServerNode().getPortNumber() + " " + noOfHops + fileNameList;
        setResponseMessage(responseMessage);
        appendMsgLength();
    }
}
