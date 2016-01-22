package com.vishlesha.search;

import com.vishlesha.dataType.Node;

import java.util.*;

/**
 * Created by bhash90 on 1/3/16.
 */
public class FileIpMapping {

   private Map<List<String>, List<Node>> wordsMap =  new HashMap<List<String>, List<Node>>(); // Ex Map(List(Lord,of,the,rings), List(1.1.1.1, 10.2.1.1))

   public synchronized void addFile(String fileNameString, Node node){
      String  tempString = fileNameString.toLowerCase();
      List<String> wordsList = Arrays.asList(tempString.split("_"));

      if(wordsMap.containsKey(wordsList)){
         List<Node> list = wordsMap.get(wordsList);
         list.add(node);
         //wordsMap.put(wordsList,list);
      }else{
         List<Node> newlist = new ArrayList<Node>();
         newlist.add(node);
         wordsMap.put(wordsList, newlist);
      }

   }

   public Map<Node, List<List<String>>> searchForFile(String query) {
      String tempString = query.toLowerCase();
      String[] queryWordsArr = tempString.split("_");
      List<String> queryWords = Arrays.asList(queryWordsArr);
      Set<List<String>> keySet = wordsMap.keySet();
      //System.out.println(keySet.size());

      Map<Node, List<List<String>>> resultIps = new HashMap<Node, List<List<String>>>();
      for (List<String> key : keySet){
         Boolean  b = key.containsAll(queryWords);

         if(b){
            List<Node> nodes =  wordsMap.get(key);
            // System.out.println(b);
            for(Node node: nodes){
               if(resultIps.containsKey(node)){
                  List<List<String>> wordsList = resultIps.get(node);
                  wordsList.add(key);
               }else{
                  List<List<String>> newlist = new ArrayList<List<String>>();
                  newlist.add(key);
                  resultIps.put(node,newlist);
               }
            }
         }
      }
       return  resultIps;
   }

}
