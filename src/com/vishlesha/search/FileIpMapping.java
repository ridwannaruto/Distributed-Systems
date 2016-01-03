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
         wordsMap.put(wordsList,list);
      }else{
         List<String> newlist = new ArrayList<String>();
         newlist.add(ip);
         wordsMap.put(wordsList,newlist);
      }


   }

   public List<String> searchForFile(String query) {
      String tempString = query.toLowerCase();
      String[] queryWordsArr = tempString.split(" ");
      List<String> queryWords = Arrays.asList(queryWordsArr);

      for (String word : queryWords){
         System.out.println(word);
      }

      Set<List<String>> keySet = wordsMap.keySet();
      System.out.println(keySet.size());

      List<String> resultIps = new ArrayList<>();
      for (List<String> key : keySet){
         Boolean  b = key.containsAll(queryWords);
         System.out.println(b);
         if(b){
            resultIps.addAll(wordsMap.get(key));
         }
      }
       return  resultIps;
   }


   public static void  main(String[] arg){
      FileIpMapping fim = new FileIpMapping();
      fim.addFile("Lord of the rings", "10.1.1.1");
      fim.addFile("Home Alone 1", "1.1.1.2");
      fim.addFile("12 years of a Slave", "1.3.2.1");
      fim.addFile("Home Alone 4", "2.1.1.3");
      fim.addFile("Home Alone 4", "2.3.3.3");

      List<String> ips = fim.searchForFile("Home Alone");
      System.out.println(ips.size());
      for(String ip : ips){
         System.out.println(ip);
      }
   }
}
