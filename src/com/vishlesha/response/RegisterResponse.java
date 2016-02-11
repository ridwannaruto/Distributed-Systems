package com.vishlesha.response;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.error.RegisterError;

import java.util.ArrayList;

/**
 * Created by ridwan on 1/1/16.
 */

public class RegisterResponse extends Response {

    private ArrayList<Node> nodeList;
    private final int noOfNodes;

    public RegisterResponse(String responseMessage, Node recipientNode) {
        setRecipientNode(recipientNode);
        String[] token = responseMessage.split(" ");
        noOfNodes = Integer.valueOf(token[2]);
        if (token[1].equals("REGOK") && noOfNodes < 9000) {
            setFail(false);
            if (!GlobalState.isTestMode())
                System.out.println(GlobalConstant.SUCCESS_MSG_REG);

            nodeList = new ArrayList<Node>();

            for (int i = 3; i < 3 + (noOfNodes * 3) && i<token.length; i += 3) {
                Node node = new Node();

                node.setIpaddress(token[i]);
                node.setPortNumber(Integer.valueOf(token[i + 1]));
                nodeList.add(node);
            }
        } else {
            setFail(true);
            RegisterError registerError = new RegisterError(responseMessage, recipientNode);
            setError(registerError);
            if (!GlobalState.isTestMode())
                System.out.println("Register Error: " + registerError.getErrorMessage());
        }
    }

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

}
