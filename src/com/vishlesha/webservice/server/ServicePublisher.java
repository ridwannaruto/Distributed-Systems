package com.vishlesha.webservice.server;

import com.vishlesha.webservice.commons.WSConstants;

import javax.xml.ws.Endpoint;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tharindu on 2/7/16.
 */
public class ServicePublisher {

	//publishing web server on given end point
	public static void publish(String host, int port) {
		String wsEndPointAddress = "http://" + host + ":" + port + "/" + WSConstants.SERVICE_NAME;
		Endpoint endpoint =Endpoint.create(new MovieFinderImpl());
		ExecutorService executorService =
				Executors.newFixedThreadPool(WSConstants.THREAD_POOL_SIZE);
		endpoint.setExecutor(executorService);
		endpoint.publish(wsEndPointAddress);
		System.out.println("Service is published at " + wsEndPointAddress);
	}
}