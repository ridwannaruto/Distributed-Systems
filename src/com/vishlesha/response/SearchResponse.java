package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.error.SearchError;
import com.vishlesha.error.UnregisterError;

/**
 * Created by ridwan on 1/1/16.
 */
public class SearchResponse extends Response {

    int responseCode;

    public SearchResponse (String responseMessage){

        String[] token = responseMessage.split(" ");
        responseCode = Integer.valueOf(token[2]);

        if (token[1].equals("SEROK") && responseCode == 0){
            setError(false);
            if (!GlobalConstant.isTestMode())
                System.out.println(GlobalConstant.MSG_SEARCH_NORESULT);
        }

        else if (token[1].equals("SEROK") && responseCode < 9000){
            setError(false);
            if (!GlobalConstant.isTestMode())
                System.out.println(GlobalConstant.SUCCESS_MSG_SEARCH);
        }

        else{
            setError(true);
            SearchError searchError = new SearchError(responseMessage);
            if (!GlobalConstant.isTestMode())
                System.out.println("Search Error: " + searchError.getErrorMessage());
        }

    }
}
