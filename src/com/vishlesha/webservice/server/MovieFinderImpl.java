package com.vishlesha.webservice.server;

import com.vishlesha.app.GlobalState;
import com.vishlesha.log.AppLogger;
import com.vishlesha.webservice.client.gen.MovieFinderImplService;
import com.vishlesha.webservice.commons.SearchResponseBean;
import com.vishlesha.webservice.model.QueuedSearchRequest;
import com.vishlesha.webservice.model.SearchContext;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by tharindu on 2/8/16.
 */

@WebService(endpointInterface = "com.vishlesha.webservice.server.MovieFinder")
public class MovieFinderImpl implements MovieFinder {

    public static Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

    @Override
    public boolean searchFile(final String ipHop, final String ip, final String port,
                              final String queryText,
                              final int hops) {
        GlobalState.incrementReceivedRequestCount();

        QueuedSearchRequest queuedSearchRequest = new QueuedSearchRequest();
        queuedSearchRequest.setInitiator(ip);
        queuedSearchRequest.setQuery(queryText);

        final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);

        if (SearchContext.processingRequest.indexOf(queuedSearchRequest) == -1) {
            SearchContext.addProcessingRequest(queuedSearchRequest);
            queuedSearchRequest.setIn(ipHop);

            ArrayList<String> files = (ArrayList<String>) SearchContext.getLocalResult(queryText);
            boolean st = false;

            if (files == null || files.size() == 0) {
                log.info("No Local result: " + queryText + "@" + ip + " from: " + ipHop);
            } else {
                GlobalState.incrementAnsweredRequestCount();
                try {
                    URL url = new URL("http://" + ip + ":8888/movieFinder?wsdl");  //URL should be changed accordingly
                    QName qname = new QName("http://server.webservice.vishlesha.com/", "MovieFinderImplService");
                    MovieFinderImplService mis = new MovieFinderImplService(url, qname);
                    com.vishlesha.webservice.client.gen.MovieFinder mf = mis.getMovieFinderImplPort();  // Get the stub
                    com.vishlesha.webservice.client.gen.SearchResponseBean sb =
                            new com.vishlesha.webservice.client.gen.SearchResponseBean();
                    sb.setPort("" + GlobalState.getLocalServerNode().getPortNumber());
                    sb.setIP(GlobalState.getLocalServerNode().getIpaddress());
                    sb.setHops(hops);
                    sb.setNoFiles(files.size());
                    sb.setFiles(new com.vishlesha.webservice.client.gen.SearchResponseBean.Files());
                    for (String s : files) {
                        sb.getFiles().getFile().add(s);
                    }
                    sb.setMessage(queryText);
                    mf.searchResult(sb);
                    st = true;

                } catch (Exception e) {
                    st = false;
                    log.info("Request Initiator is down");
                }
            }

            if (hops < SearchContext.maxHopCount) {
                log.info("forwarding flow started :" + queryText + "@" + ip + " from:" + ipHop);

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    public void run() {
                        SearchContext.forwardSearchRequest(ipHop, ip, port, queryText, hops + 1);
                    }
                });

            } else {
                log.info("Max hop count reached :" + queryText + "@" + ip + " from:" + ipHop);
                st = false;
            }

            return st;

        } else {
            log.info("Message already processing " + queryText + "@" + ip + " from:" + ipHop);
            return false;
        }

    }

    @Override
    public void searchResult(SearchResponseBean resp) {
        long currentTime = System.currentTimeMillis();
        SearchContext.QueryMeta query = null;
        String queryText = resp.getMessage();

        //  search for query in history
        for (SearchContext.QueryMeta q : SearchContext.searchHistory.keySet()) {
            if (queryText.equals(q.getQuery())) {
                query = q;
                break;
            }
        }

        // unknown query?
        if (query == null) {
            log.warning("Unknown query result: " + resp);
            return;
        }

        // compute latency from query initiation
        long latency = currentTime - query.getTimestamp();

        // add results
        Map<String, Vector<SearchContext.ResultMeta>> resultMap = SearchContext.searchHistory.get(query);
        for (String fname : resp.getFileNames()) {
            Vector<SearchContext.ResultMeta> results = resultMap.get(fname);
            if (results == null) {
                results = new Vector<>();
                resultMap.put(fname, results);
            }

            results.add(new SearchContext.ResultMeta(resp.getIP(), resp.getHops(), latency));
        }

        SearchContext.showResult(query);
    }

    @Override
    public void heartBeat() {
    }
}
