package com.vishlesha.search;

import java.util.*;

/**
 * Created by bhash90 on 1/3/16.
 */
public class FileIpMapping {

   private Map<List<String>, List<String>> wordsMap =  new HashMap<List<String>, List<String>>();

   public synchronized void addFile(String fileNameString, String ip){
      String  tempString = fileNameString.toLowerCase();
      List<String> wordsList = Arrays.asList(tempString.split(" "));

      if(wordsMap.containsKey(wordsList)){
         List<String> list = wordsMap.get(wordsList);
         list.add(ip);
         //wordsMap.put(wordsList,list);
      }else{
         List<String> newlist = new ArrayList<String>();
         newlist.add(ip);
         wordsMap.put(wordsList, newlist);
      }

   }

   public Map<String, List<List<String>>> searchForFile(String query) {
      String tempString = query.toLowerCase();
      String[] queryWordsArr = tempString.split(" ");
      List<String> queryWords = Arrays.asList(queryWordsArr);
      Set<List<String>> keySet = wordsMap.keySet();
      //System.out.println(keySet.size());

      Map<String, List<List<String>>> resultIps = new HashMap<String, List<List<String>>>();
      for (List<String> key : keySet){
         Boolean  b = key.containsAll(queryWords);

         if(b){
            List<String> ips =  wordsMap.get(key);
            // System.out.println(b);
            for(String ip: ips){
               if(resultIps.containsKey(ip)){
                  List<List<String>> wordsList = resultIps.get(ip);
                  wordsList.add(key);
               }else{
                  List<List<String>> newlist = new ArrayList<List<String>>();
                  newlist.add(key);
                  resultIps.put(ip,newlist);
               }
            }
         }
      }
       return  resultIps;
   }

}
