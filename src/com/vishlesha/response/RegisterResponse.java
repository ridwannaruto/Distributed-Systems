package com.vishlesha.response;

import com.vishlesha.dataType.Node;
import com.vishlesha.error.RegisterError;

import java.util.ArrayList;

/**
 * Created by ridwan on 1/1/16.
 */

public class RegisterResponse extends Response {

    ArrayList<Node> nodeList;
    int noOfNodes;

    public RegisterResponse(String responseMessage){

        String[] token = responseMessage.split(" ");
        noOfNodes = Integer.valueOf(token[2]);

        if (token[1].equals("REGOK") && noOfNodes <9000){
            setError(false);
            if (!globalConstant.isTestMode())
                System.out.println(globalConstant.REGISTER_SUCCESS_RESPONSE);

            nodeList = new ArrayList<Node>();

            for (int i=3; i< 3+ (noOfNodes*3); i+=3){
                Node node = new Node();
                node.setIpaddress(token[i]);
                node.setPortNumber(Integer.valueOf(token[i + 1]));
                nodeList.add(node);
            }
        }else{
            setError(true);
            RegisterError registerError = new RegisterError(responseMessage);
            if (!globalConstant.isTestMode())
                System.out.println("Register Error: " + registerError.getErrorMessage());
        }



    }

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }
}
