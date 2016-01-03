package com.vishlesha.request;

import com.vishlesha.dataType.Node;

import java.util.List;

/**
 * Created by ridwan on 1/1/16.
 */
public class FileListShareRequest extends Request {

    private List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public FileListShareRequest(Node node, List<String> files){
        setRecepientNode(node);
        setFiles(files);
        StringBuilder builder = new StringBuilder(" FILES ");
        for (String file: files) {
            builder.append(file).append(" ");   // FIXME assuming no spaces in file names
        }
        requestMessage = builder.substring(0, builder.length() - 1);    // ignore last ' '
        appendMsgLength();
    }
}
