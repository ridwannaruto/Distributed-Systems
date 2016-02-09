package com.vishlesha.error;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class SearchError extends Error {

    public SearchError(String errorMessage, Node errorNode) {
        super(errorNode);
        String[] token = errorMessage.split(" ");

        if (token[1].equals("ERROR")) {
            setErrorCode(0);
            setErrorMessage(GlobalConstant.ERR_MSG_GENERAL);
        } else {
            int errorCode = Integer.valueOf(token[2]);
            switch (errorCode) {
                case 9999:
                    setErrorCode(GlobalConstant.ERR_CODE_SEARCH_UNREACHABLE);
                    setErrorMessage(GlobalConstant.ERR_MSG_SEARCH_UNREACHABLE);
                    break;

                case 9998:
                    setErrorCode(GlobalConstant.ERR_CODE_SEARCH_GENERAL);
                    setErrorMessage(GlobalConstant.ERR_MSG_SEARCH_GENERAL);
                    break;
            }
        }
    }
}
