package com.vishlesha.timer.task;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.error.SearchError;
import com.vishlesha.log.AppLogger;
import com.vishlesha.response.SearchResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/22/16.
 */
public class PrintResponseTask extends TimerTask {

    private final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    @Override
    public void run() {
        long initiatedTimeStamp = GlobalState.getCurrentSearchingRequest().getTimestamp();
        try {
            System.out.println("\nSearch Responses for request " + GlobalState.getCurrentSearchingRequest().getQuery());
            System.out.println("----------------------------------------------------");
            Map<String,SearchResponse> responseList = new HashMap<>();
            responseList.putAll(GlobalState.getSearchResponseList());
            for (SearchResponse response: responseList.values()){
                System.out.println("\nResponse from " + response.getSenderNode().getIpaddress() + " hops: " +
                        response.getNoOfHops() + " latency: "+ (response.getTimestamp() - initiatedTimeStamp) + " ms");
                String responseMessage = response.getResponseMessage();

                String[] token = responseMessage.split(" ");
                int responseCode = Integer.valueOf(token[2]);

                if (token[1].equals("SEROK") && responseCode == 0) {
                    if (!GlobalState.isTestMode())
                        System.out.println(GlobalConstant.MSG_SEARCH_NORESULT);
                } else if (token[1].equals("SEROK") && responseCode < 9000) {
                    List<String> fileList = response.getFileList();
                    if (!GlobalState.isTestMode()) {
                        for (String aFileList : fileList) {
                            System.out.println(aFileList);
                        }
                    }

                } else {
                    SearchError searchError = new SearchError(responseMessage, response.getSenderNode());
                    if (!GlobalState.isTestMode())
                        System.out.println("Search Error: " + searchError.getErrorMessage());
                }

            }

            GlobalState.setCurrentSearchingRequest(null);
            GlobalState.clearResponseList();

        } catch (Exception ex) {
            log.severe("response list print task error");
        }
    }
}
