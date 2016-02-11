package com.vishlesha.webservice.model;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.FileIpMapping;
import com.vishlesha.dataType.Node;
import com.vishlesha.webservice.client.gen.MovieFinder;
import com.vishlesha.webservice.client.gen.MovieFinderImplService;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by tharindu on 2/8/16.
 */
public class SearchContext {

	public static List<QueuedSearchRequest> processingRequest = new Vector<QueuedSearchRequest>();
	public static int maxHopCount = 10;

	public static void addProcessingRequest(QueuedSearchRequest qsr)
	{
		processingRequest.add(qsr);
		qsr.setIndex(processingRequest.indexOf(qsr));
	}
	public static boolean forwardSearchRequest(String inIp, String ip, String port,
	                                           String request,
	                                           int numHops) {
		boolean forwarded = false;

		System.out.printf("\n\n\nForward request...\n\n");
		Map<Node, List<String>> neighbors = new Hashtable<>();
		neighbors.putAll(GlobalState.getNeighbors());
		QueuedSearchRequest queuedSearchRequest = new QueuedSearchRequest();
		queuedSearchRequest.setInitiator(ip);
		queuedSearchRequest.setQuery(request);
		System.out.println("");
		QueuedSearchRequest qre = SearchContext.processingRequest
				.get(SearchContext.processingRequest.indexOf(queuedSearchRequest));

		for (Node neighbor : neighbors.keySet()) {
			URL url = null;
			try {
				if (!neighbor.getIpaddress().equals(inIp)) {

					url = new URL("http://" + neighbor.getIpaddress() +
					              ":8888/movieFinder?wsdl");  //URL should be changed accordingly
					QName qname =
							new QName("http://server.webservice.vishlesha.com/",
							          "MovieFinderImplService");
					qre.getOut().add(neighbor.getIpaddress());
					MovieFinderImplService mis = new MovieFinderImplService(url, qname);
					MovieFinder mf = mis.getMovieFinderImplPort();  // Get the stub

					boolean newStatus=mf.searchFile(GlobalState.getLocalServerNode().getIpaddress(), ip, port,
					                                request,
					                                numHops + 1); //call service methods
					//			System.out.println(neighbor.toString() + sr.getFiles());

					if(newStatus)
					{
						forwarded=newStatus;
					}

					System.out.println(request + "@" + ip + " to" + neighbor.toString());
					forwarded = true;
				}else
				{
					System.out.println(neighbor.toString()+" is the incomming! "+request + "@" + ip + " to" + neighbor.toString());
				}

			} catch (javax.xml.ws.WebServiceException e) {
				System.out.println(neighbor+" Is DOWN");
				//				neighbors.remove(neighbor);
				GlobalState.getNeighbors().remove(neighbor);
				if(!forwarded)
					forwarded=false;
			}catch (Exception e){
				e.printStackTrace();
				if(!forwarded)
					forwarded=false;
			}

		}

		return forwarded;
	}

	public static void initiateSearch(String ip, String port, String searchQuery, int numHops) {
		System.out.println("\nLocal search result\n----------------------");
		List<String> localResult = getLocalResult(searchQuery);
		if (localResult.isEmpty()) {
			System.out.println("no matching result found");
		} else {
			for (int i = 0; i < localResult.size(); i++)
				System.out.printf(localResult.get(i));
		}
		System.out.printf("\n\n\nsearching for file in network ......\n\n");
		QueuedSearchRequest queuedSearchRequest = new QueuedSearchRequest();
		queuedSearchRequest.setInitiator(ip);
		queuedSearchRequest.setQuery(searchQuery);
		SearchContext.addProcessingRequest(queuedSearchRequest);
		System.out.println("IP" + ip + " " + searchQuery);
		forwardSearchRequest(ip, ip, port, searchQuery, numHops);
	}

	public static List<String> getLocalResult(String searchQuery) {
        FileIpMapping fileIpMapping = GlobalState.getFileIpMapping();
        Map<Node, List<String>> fileMap = fileIpMapping.searchForFile(searchQuery);
        List<String> fileList = fileMap.get(GlobalState.getLocalServerNode());
        if (fileList == null) {
            fileList = Collections.EMPTY_LIST;
        }
        return fileList;
	}
}
