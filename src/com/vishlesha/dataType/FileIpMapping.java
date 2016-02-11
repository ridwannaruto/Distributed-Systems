package com.vishlesha.dataType;

import java.util.*;

/**
 * Created by bhash90 on 1/3/16.
 */
public class FileIpMapping {

    // maps each file (represented as a list of words in the file name) to a list of nodes containing it
    private final Map<Filename, List<Node>> wordsMap = new HashMap<>(); // Ex Map(List(Lord,of,the,rings), List(1.1.1.1, 10.2.1.1))

    public synchronized void addFile(String fileNameString, Node node) {
        String tempString = fileNameString.toLowerCase();
        List<String> wordsList = Arrays.asList(tempString.split("_"));  //TODO use regex

        if (wordsMap.containsKey(wordsList)) {
            List<Node> list = wordsMap.get(wordsList);
            list.add(node);
            //wordsMap.put(wordsList,list);
        } else {
            List<Node> newlist = new ArrayList<Node>();
            newlist.add(node);
            wordsMap.put(new Filename(fileNameString, wordsList), newlist);
        }

    }

    public Map<Node, List<String>> searchForFile(String query) {
        String tempString = query.toLowerCase();
        String[] queryWordsArr = tempString.split("_");
        List<String> queryWords = Arrays.asList(queryWordsArr);
        Set<Filename> keySet = wordsMap.keySet();
        //System.out.println(keySet.size());

        Map<Node, List<String>> resultIps = new HashMap<>();
        for (Filename key : keySet) {
            Boolean b = key.getWords().containsAll(queryWords);

            if (b) {
                List<Node> nodes = wordsMap.get(key);
                // System.out.println(b);
                for (Node node : nodes) {
                    if (resultIps.containsKey(node)) {
                        List<String> wordsList = resultIps.get(node);
                        wordsList.add(key.getName());
                    } else {
                        List<String> newlist = new ArrayList<>();
                        newlist.add(key.getName());
                        resultIps.put(node, newlist);
                    }
                }
            }
        }
        return resultIps;
    }

    private static class Filename {
        private String name;
        private List<String> words;

        public Filename(String name, List<String> words) {
            this.name = name;
            this.words = words;
        }

        public List<String> getWords() {
            return words;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filename filename = (Filename) o;

            return (name == null ? filename.name == null : name.equals(filename.name));
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}
