package com.vishlesha.dataType;

/**
 * Created by ridwan on 1/1/16.
 */
public class Node {
    String ipaddress;
    int portNumber;

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

        if (portNumber != node.portNumber) return false;
        if (!ipaddress.equals(node.ipaddress)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ipaddress.hashCode();
        result = 31 * result + portNumber;
        return result;
    }
}
