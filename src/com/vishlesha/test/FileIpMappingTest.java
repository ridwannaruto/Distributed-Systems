package com.vishlesha.test;

import java.util.List;
import java.util.Map;

import com.vishlesha.dataType.Node;
import com.vishlesha.dataType.FileIpMapping;

/**
 * Created by bhash90 on 1/3/16.
 */
public class FileIpMappingTest {

   FileIpMapping fim = new FileIpMapping();

   public static void  main(String[] arg){
      FileIpMappingTest fimt = new FileIpMappingTest();

      fimt.testAddFile("Lord of the rings", new Node("10.1.4.1", 1057));
      fimt.testAddFile("Home Alone 1",new Node("1.1.1.2", 1056) );
      fimt.testAddFile("12 years of a Slave", new Node("10.1.1.1", 1057));
      fimt.testAddFile("Home Alone 4", new Node("2.1.1.3", 1052));
      fimt.testAddFile("Home Alone 4", new Node("2.3.3.3", 1054));

      //Todo Write Test for AddFile Method

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

   private void testAddFile(String fileName, Node node) {
      fim.addFile(fileName, node);
   }

   private int testQuery(String query){
      Map<Node,List<List<String>>> nodes = fim.searchForFile(query);
      int size = nodes.size();
      System.out.println(size);
      for(Node node : nodes.keySet()){
         System.out.println(node);
      }
      return nodes.size();
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
