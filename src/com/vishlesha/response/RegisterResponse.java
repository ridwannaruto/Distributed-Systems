package com.vishlesha.response;

import com.vishlesha.dataType.Node;

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
            System.out.println(globalConstant.REGISTER_SUCCESS_RESPONSE);
        }

        nodeList = new ArrayList<Node>();

        for (int i=3; i< 3+ (noOfNodes*3); i+=3){
            Node node = new Node();
            node.setIpaddress(token[i]);
            node.setPortNumber(Integer.valueOf(token[i + 1]));
            nodeList.add(node);
        }

    }

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }
}
