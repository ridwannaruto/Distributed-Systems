package com.vishlesha.dataType;

/**
 * Created by ridwan on 1/1/16.
 */
public class Node {
    private String ipaddress;
    private int portNumber;

    public Node() {
    }

    public Node(String ipaddress, int portNumber) {
        this.ipaddress = ipaddress;
        this.portNumber = portNumber;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;
        return ipaddress.equals(node.ipaddress);
    }

    @Override
    public int hashCode() {
        return ipaddress.hashCode();
    }
}
