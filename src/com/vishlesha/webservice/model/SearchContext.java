package com.vishlesha.webservice.model;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.FileIpMapping;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.webservice.client.gen.MovieFinder;
import com.vishlesha.webservice.client.gen.MovieFinderImplService;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by tharindu on 2/8/16.
 */
public class SearchContext {

    public static List<QueuedSearchRequest> processingRequest = new Vector<QueuedSearchRequest>();
    public static Map<QueryMeta, Map<String, Vector<ResultMeta>>> searchHistory = new Hashtable<>();
    public static Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    public static int maxHopCount = 10;

    public static void addProcessingRequest(QueuedSearchRequest qsr) {
        processingRequest.add(qsr);
        qsr.setIndex(processingRequest.indexOf(qsr));
    }

    public static boolean forwardSearchRequest(String inIp, String ip, String port,
                                               String request,
                                               int numHops) {
        boolean forwarded = false;

        Map<Node, List<String>> neighbors = new Hashtable<>();
        neighbors.putAll(GlobalState.getNeighbors());
        QueuedSearchRequest queuedSearchRequest = new QueuedSearchRequest();
        queuedSearchRequest.setInitiator(ip);
        queuedSearchRequest.setQuery(request);
        QueuedSearchRequest qre = SearchContext.processingRequest
                .get(SearchContext.processingRequest.indexOf(queuedSearchRequest));

        for (Node neighbor : neighbors.keySet()) {
            URL url;
            try {
                if (!neighbor.getIpaddress().equals(inIp)) {
                    GlobalState.incrementForwardedRequestCount();

                    url = new URL("http://" + neighbor.getIpaddress() +
                            ":8888/movieFinder?wsdl");  //URL should be changed accordingly
                    QName qname = new QName("http://server.webservice.vishlesha.com/", "MovieFinderImplService");
                    qre.getOut().add(neighbor.getIpaddress());
                    MovieFinderImplService mis = new MovieFinderImplService(url, qname);
                    MovieFinder mf = mis.getMovieFinderImplPort();  // Get the stub

                    boolean newStatus = mf.searchFile(GlobalState.getLocalServerNode().getIpaddress(), ip, port,
                            request,
                            numHops + 1); //call service methods

                    if (newStatus) {
                        forwarded = newStatus;
                    }

                    log.info(request + "@" + ip + " to " + neighbor.toString());
                    forwarded = true;

                } else {
                    log.info(
                            neighbor.toString() + " is the incomming! " + request + "@" + ip +
                                    " to" + neighbor.toString());
                }

            } catch (javax.xml.ws.WebServiceException e) {
                System.out.println(neighbor + " Is DOWN");
                GlobalState.getNeighbors().remove(neighbor);
                if (!forwarded)
                    forwarded = false;

            } catch (Exception e) {
                e.printStackTrace();
                if (!forwarded)
                    forwarded = false;
            }

        }

        return forwarded;
    }

    public static void showResult(QueryMeta query) {
        System.out.println("Query: " + query.getQuery());
        Set<String> s = searchHistory.get(query).keySet();
        for (String file : s) {
            System.out.println("\t File: " + file);

            Vector<ResultMeta> nodes = searchHistory.get(query).get(file);
            for (ResultMeta result : nodes) {
                System.out.format("\t\t%-15s %2d (%d ms)\n", result.getIp(), result.getNumHops(), result.getLatency());
            }
        }
    }

    public static void initiateSearch(String ip, String port, String queryText, int numHops) {
        long currentTime = System.currentTimeMillis();
        QueryMeta query = new QueryMeta(queryText, currentTime);

        if (searchHistory.get(query) != null)
            searchHistory.remove(query);
        searchHistory.put(query, new Hashtable<String, Vector<ResultMeta>>());

        System.out.println("\nLocal search result\n----------------------");
        List<String> localResult = getLocalResult(queryText);
        long localQueryTime = System.currentTimeMillis() - currentTime;

        if (localResult.isEmpty()) {
            System.out.println("no matching result found");
        } else {
            Map<String, Vector<ResultMeta>> resultMap = searchHistory.get(query);
            for (String resultText : localResult) {
                Vector<ResultMeta> results = resultMap.get(resultText);
                if (results == null) {
                    results = new Vector<>();
                    resultMap.put(resultText, results);
                }

                results.add(new ResultMeta(GlobalState.getLocalServerNode().getIpaddress(), 0, localQueryTime));
            }
        }
        showResult(query);

        System.out.printf("\n\n\nsearching for file in network ......\n\n");
        QueuedSearchRequest queuedSearchRequest = new QueuedSearchRequest();
        queuedSearchRequest.setInitiator(ip);
        queuedSearchRequest.setQuery(queryText);
        SearchContext.addProcessingRequest(queuedSearchRequest);
        System.out.println("IP " + ip + " " + queryText);
        forwardSearchRequest(ip, ip, port, queryText, numHops);
    }

    public static List<String> getLocalResult(String searchQuery) {
        FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
        Map<Node, List<String>> fileMap = fileIpMapping.searchForFile(searchQuery);
        List<String> fileList = fileMap.get(GlobalState.getLocalServerNode());
        if (fileList == null) {
            fileList = new ArrayList<>();
        }
        return fileList;
    }

    /** Holds metadata of a search result */
    public static class ResultMeta {
        private String ip;
        private int numHops;
        private long latency;

        public ResultMeta(String ip, int numHops, long latency) {
            this.ip = ip;
            this.numHops = numHops;
            this.latency = latency;
        }

        public String getIp() {
            return ip;
        }

        public int getNumHops() {
            return numHops;
        }

        public long getLatency() {
            return latency;
        }
    }

    /** Holds metadata of a search query */
    public static class QueryMeta {
        private String query;
        private long timestamp;

        public QueryMeta(String query, long timestamp) {
            this.query = query;
            this.timestamp = timestamp;
        }

        public String getQuery() {
            return query;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            QueryMeta queryMeta = (QueryMeta) o;

            if (query != null ? !query.equals(queryMeta.query) : queryMeta.query != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = query != null ? query.hashCode() : 0;
            result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
            return result;
        }
    }
}
