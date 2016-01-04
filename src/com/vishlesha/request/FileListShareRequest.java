package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

import java.util.ArrayList;
import java.util.List;

// 00xx FILES IP port file1 file2 ...
public class FileListShareRequest extends Request {

    private List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    // response sent out by this node
    public FileListShareRequest(Node node, List<String> files){
        setRecepientNode(node);
        setFiles(files);
        StringBuilder builder = new StringBuilder(" FILES ");
        builder.append(GlobalState.getLocalServerNode().getIpaddress())
                .append(" ")
                .append(GlobalState.getLocalServerNode().getPortNumber())
                .append(" ");
        for (String file: files) {
            builder.append(file).append(" ");   // FIXME assuming no spaces in file names
        }
        requestMessage = builder.substring(0, builder.length() - 1);    // ignore last ' '
        appendMsgLength();
    }

    // request from a different node
    public FileListShareRequest(String requestMessage){
        String[] tokens = requestMessage.split(" ");
        Node node = new Node();
        node.setIpaddress(tokens[KEY_IP_ADDRESS]);
        node.setPortNumber(Integer.valueOf(tokens[KEY_PORT_NUM]));
        setRecepientNode(node);

        List<String> files = new ArrayList<>();
        for (int i = KEY_PORT_NUM + 1; i < tokens.length; i++) {
            files.add(tokens[i]);
        }
        setFiles(files);
    }
}
