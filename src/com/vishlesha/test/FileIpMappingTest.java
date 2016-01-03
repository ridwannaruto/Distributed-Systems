package com.vishlesha.test;

import java.util.List;
import com.vishlesha.search.FileIpMapping;

/**
 * Created by bhash90 on 1/3/16.
 */
public class FileIpMappingTest {

   FileIpMapping fim = new FileIpMapping();

   public static void  main(String[] arg){
      FileIpMappingTest fimt = new FileIpMappingTest();
      fimt.testAddFile("Lord of the rings", "10.1.1.1");
      fimt.testAddFile("Home Alone 1", "1.1.1.2");
      fimt.testAddFile("12 years of a Slave", "1.3.2.1");
      fimt.testAddFile("Home Alone 4", "2.1.1.3");
      fimt.testAddFile("Home Alone 4", "2.3.3.3");

      //Todo Write Test for test AddFile Method

      int home_alone = fimt.testQuery("Home Alone");
      fimt.testEqual(String.valueOf(home_alone),String.valueOf(3));
      int home_alone_4  = fimt.testQuery("Home Alone 4");
      fimt.testEqual(String.valueOf(home_alone_4),String.valueOf(2));

      int rings_of_the_lord = fimt.testQuery("Rings of the lord");
      fimt.testEqual(String.valueOf(rings_of_the_lord),String.valueOf(1));

      int rings_of_the_slave =fimt.testQuery("Rings of the slave");
      fimt.testEqual(String.valueOf(rings_of_the_slave),String.valueOf(0));

      int of = fimt.testQuery("of");
      fimt.testEqual(String.valueOf(of),String.valueOf(2));
   }

   private void testAddFile(String fileName, String ip) {
      fim.addFile(fileName, ip);
   }

   private int testQuery(String query){
      List<String> ips = fim.searchForFile(query);
      int size = ips.size();
      /*System.out.println(ips.size());
      for(String ip : ips){
         System.out.println(ip);
      }*/
      return ips.size();
   }

   private boolean testEqual(String results, String expected){

      if(results.equals(expected)){
         System.out.println("Succeed");
         return true;
      }else{
         System.out.println("Failiure");
         return false;
      }
   }
}
