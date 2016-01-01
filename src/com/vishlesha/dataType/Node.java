package com.vishlesha.dataType;

/**
 * Created by ridwan on 1/1/16.
 */
public class Node {
    String ipaddress;
    int portNumber;

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public String toString() {
        return "Node{" +
                "ipaddress='" + ipaddress + '\'' +
                ", portNumber=" + portNumber +
                '}';
    }
}
