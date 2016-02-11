package com.vishlesha.webservice.model;

import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.FileIpMapping;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.webservice.client.gen.MovieFinder;
import com.vishlesha.webservice.client.gen.MovieFinderImplService;
import com.vishlesha.webservice.client.gen.SearchResponseBean;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by tharindu on 2/8/16.
 */
public class SearchContext {

	public static List<QueuedSearchRequest> processingRequest = new Vector<QueuedSearchRequest>();
	public static Map<String, Map<String, Vector<IPhopes>>> searchHistory = new Hashtable<>();
	public static Vector<String> queryHistory=new Vector<>();
	public static Logger log= Logger.getLogger(AppLogger.APP_LOGGER_NAME);

	public static int maxHopCount = 10;
	public static void addProcessingRequest(QueuedSearchRequest qsr) {
		processingRequest.add(qsr);
		qsr.setIndex(processingRequest.indexOf(qsr));
	}

	public static boolean forwardSearchRequest(String inIp, String ip, String port,
	                                           String request,
	                                           int numHops) {
		boolean forwarded = false;

		//		System.out.printf("\n\n\nForward request...\n\n");
		Map<Node, List<String>> neighbors = new Hashtable<>();
		neighbors.putAll(GlobalState.getNeighbors());
		QueuedSearchRequest queuedSearchRequest = new QueuedSearchRequest();
		queuedSearchRequest.setInitiator(ip);
		queuedSearchRequest.setQuery(request);
		//		System.out.println("");
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

					boolean newStatus =
							mf.searchFile(GlobalState.getLocalServerNode().getIpaddress(), ip, port,
							              request,
							              numHops + 1); //call service methods
					//			System.out.println(neighbor.toString() + sr.getFiles());

					if (newStatus) {
						forwarded = newStatus;
					}

					log.info(request + "@" + ip + " to" + neighbor.toString());
					forwarded = true;
				} else {
					log.info(
							neighbor.toString() + " is the incomming! " + request + "@" + ip +
							" to" + neighbor.toString());
				}

			} catch (javax.xml.ws.WebServiceException e) {
				System.out.println(neighbor + " Is DOWN");
				//				neighbors.remove(neighbor);
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

	public static void showResult(String query)
	{
		System.out.println("Query:"+query);
		Set s=searchHistory.get(query).keySet();
		Iterator it=s.iterator();
		while(it.hasNext())
		{
			String f=(String)it.next();

			System.out.println("\t File:"+f);

			Vector<IPhopes> nodes=searchHistory.get(query).get(f);

			for(int i=0;i<nodes.size();i++)
			{
				System.out.println("\t\t"+nodes.get(i).IP+" "+nodes.get(i).numHopes);

			}
		}

	}
	public static void initiateSearch(String ip, String port, String searchQuery, int numHops) {

		if (searchHistory.get(searchQuery) != null)
			searchHistory.remove(searchQuery);
		else
			searchHistory.put(searchQuery, new Hashtable<String, Vector<IPhopes>>());

		queryHistory.add(searchQuery);

		System.out.println("\nLocal search result\n----------------------");
		List<String> localResult = getLocalResult(searchQuery);

		if (localResult.isEmpty()) {
			System.out.println("no matching result found");
		} else {
			for (int i = 0; i < localResult.size(); i++) {
				//System.out.printf(localResult.get(i));

				if (searchHistory.get(searchQuery).get(localResult.get(i))==null)
				{
					searchHistory.get(searchQuery).put(localResult.get(i), new Vector<IPhopes>());
				}

				((Vector)(searchHistory.get(searchQuery).get(localResult.get(i)))).add(new IPhopes(GlobalState.getLocalServerNode().getIpaddress(),0));

			}
		}
		showResult(searchQuery);
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
		Map<Node, List<List<String>>> fileMap = fileIpMapping.searchForFile(searchQuery);
		List<List<String>> fileListList;
		List<String> fileList = new ArrayList<String>();
		try {
			fileListList = fileMap.get(GlobalState.getLocalServerNode());
			StringBuilder s = new StringBuilder();
			if (fileListList != null) {
				for (List<String> stringList : fileListList) {
					for (String string : stringList) {
						s.append(string);
						s.append(" ");
					}
					fileList.add(s.toString().trim());
					//System.out.println(s);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileList;
	}

	public static class IPhopes {
		String IP;
		int numHopes;

		public IPhopes(String IP, int numHopes) {
			this.IP = IP;
			this.numHopes = numHopes;
		}
	}
}
