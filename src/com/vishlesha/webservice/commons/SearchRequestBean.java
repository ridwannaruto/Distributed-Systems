package com.vishlesha.webservice.commons;

import javax.xml.bind.annotation.*;

/**
 *
 * Created by tharindu on 2/8/16.
 *
 */

@XmlType(name = "SearchRequestBean", propOrder = { "message", "IP", "port", "hops" })
@XmlRootElement(name = "SearchRequestBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchRequestBean {

	@XmlElement(name = "message")
	private String message = "SER";

	@XmlElement(name = "IP")
	private String IP = "";

	@XmlElement(name = "port")
	private String port = "";

	@XmlElement(name = "hops")
	private int hops = 0;

	public SearchRequestBean() {
	}

	public String getMessage() {
		return message;
	}

	public String getIP() {
		return IP;
	}

	public String getPort() {
		return port;
	}

	public int getHops() {
		return hops;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setHops(int hopes) {
		this.hops = hops;
	}

}


