package com.vishlesha.request;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;

import java.util.List;

/**
 * Created by ridwan on 1/1/16.
 */
public class SearchResponseRequest extends Request {

    private List<String> results;
    private int noOfHops;


   public SearchResponseRequest(){
   }

   public List<String> getResults() {
      return results;
   }

   public void setResults(List<String> results) {
      this.results = results;
   }

   public SearchResponseRequest(String requestMessage){
      String[] token = requestMessage.split(" ");
      Node node = new Node();
      node.setIpaddress(token[KEY_IP_ADDRESS]);
      node.setPortNumber(Integer.valueOf(token[KEY_PORT_NUM]));
      setSender(node);
   }


    public int getNoOfHops() {
        return noOfHops;
    }

    public void setNoOfHops(int noOfHops) {
        this.noOfHops = noOfHops;
    }


}
