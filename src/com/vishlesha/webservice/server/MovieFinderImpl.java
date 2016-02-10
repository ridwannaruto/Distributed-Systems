package com.vishlesha.webservice.server;
import com.vishlesha.app.GlobalState;
import com.vishlesha.response.SearchResponse;
import com.vishlesha.webservice.client.gen.MovieFinderImplService;
import com.vishlesha.webservice.commons.SearchResponseBean;
import com.vishlesha.webservice.commons.SearchRequestBean;
import com.vishlesha.webservice.commons.SearchResponseBean;
import com.vishlesha.webservice.model.QueuedSearchRequest;
import com.vishlesha.webservice.model.SearchContext;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.spi.http.HttpContext;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.jws.*;
import javax.xml.ws.*;
import com.sun.net.httpserver.*;

/**
 * Created by tharindu on 2/8/16.
 */

@WebService(endpointInterface = "com.vishlesha.webservice.server.MovieFinder")
public class MovieFinderImpl implements MovieFinder {

	//	@Resource
	//	WebServiceContext wsctx;

	@Resource
	private WebServiceContext wsctx;

	@Override public boolean searchFile(String ipHop,String ip, String port, String fileName, int hops) {

		QueuedSearchRequest queuedSearchRequest = new QueuedSearchRequest();
		queuedSearchRequest.setInitiator(ip);
		queuedSearchRequest.setQuery(fileName);

		if (SearchContext.processingRequest.indexOf(queuedSearchRequest) == -1) {
			SearchContext.addProcessingRequest(queuedSearchRequest);

			queuedSearchRequest.setIn(ipHop);
			MessageContext mctx = wsctx.getMessageContext();
			HttpExchange h=(HttpExchange)mctx.get("com.sun.xml.internal.ws.http.exchange");

			//		System.out.println(h+" "+h.getRemoteAddress().getHostString());


			//			System.out.println(exchange);
			//			Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);
			//			SOAPMessageContext soapContext = (SOAPMessageContext) wsctx.getMessageContext();
			//			HttpExchange exchange = (HttpExchange) soapContext.get("com.sun.xml.ws.http.exchange");
			//			InetSocketAddress remoteAddress = exchange.getRemoteAddress();
			//			String remoteHost = remoteAddress.getAddress().toString();

			ArrayList<String> files = (ArrayList<String>) SearchContext.getLocalResult(fileName);
			boolean st = false;
			if (files == null || files.size() == 0) {
				System.out.println("No Local result: " + fileName + "@" + ip+" from:"+ipHop);
			} else {

				try {

					URL url = new URL("http://" + ip +":8888/movieFinder?wsdl");  //URL should be changed accordingly
					QName qname =
							new QName("http://server.webservice.vishlesha.com/",
							          "MovieFinderImplService");
					MovieFinderImplService mis = new MovieFinderImplService(url, qname);


					com.vishlesha.webservice.client.gen.MovieFinder mf =
							mis.getMovieFinderImplPort();  // Get the stub

					com.vishlesha.webservice.client.gen.SearchResponseBean sb=new com.vishlesha.webservice.client.gen.SearchResponseBean();
					sb.setPort(""+GlobalState.getLocalServerNode().getPortNumber());
					sb.setIP(GlobalState.getLocalServerNode().getIpaddress());
					sb.setHops(hops);
					sb.setNoFiles(files.size());
					sb.setFiles(new com.vishlesha.webservice.client.gen.SearchResponseBean.Files());
					for(String s : files){
						sb.getFiles().getFile().add(s);
					}
					sb.setMessage("SEROK");

					mf.searchResult(sb);

					System.out.println(
							"Result sending flow started:" + fileName + "@" + ip + " from:" +
							ipHop);
					st = true;
				}catch (Exception e)
				{
					st=false;
					System.out.println("Request Initiator is down");
				}
			}

			if(hops<SearchContext.maxHopCount) {
				System.out.println("forwarding flow started :" + fileName + "@" + ip+" from:"+ipHop);
				st=SearchContext.forwardSearchRequest(ipHop,ip, port, fileName, hops + 1);
			}else
			{
				System.out.println("Max hope count reached :" + fileName + "@" + ip+" from:"+ipHop);
				//NEED TO CLEAR incomming path
				st=false;

			}

			return st;
		} else {
			System.out.println("Message already processing " + fileName + "@" + ip+" from:"+ipHop);
			return false;
		}

	}

	@Override public void searchResult(SearchResponseBean resp) {
		System.out.println("IP:"+resp.getIP()+"\nPort"+resp.getPort()+"\nFiles:"+resp.getFileNames().toString());
	}

	@Override public void heartBeat() {

	}

}
