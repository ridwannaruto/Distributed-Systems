package com.vishlesha.webservice.server;

import com.vishlesha.webservice.commons.SearchResponseBean;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebServiceContext;

/**
 * Created by tharindu on 2/7/16.
 */

@WebService
@SOAPBinding(style = Style.RPC)
public interface MovieFinder {
	@WebMethod
	public boolean searchFile(@WebParam(name = "ipHop") String ipHop,@WebParam(name = "ip") String ip,@WebParam(name = "port") String port, @WebParam(name = "fileName")String fileName, @WebParam(name = "hops")int hops);
	@WebMethod
	public void searchResult(@WebParam(name="SearchResponseBean")SearchResponseBean resp);
	@WebMethod
	public void heartBeat();
}
