package com.vishlesha.webservice.commons;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Created by tharindu on 2/8/16.
 */

@XmlType(name = "SearchResponseBean", propOrder = { "message", "noFiles", "IP", "port", "hops", "fileNames"})
@XmlRootElement(name = "SearchResponseBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchResponseBean {

	@XmlElement(name = "message")
	private String message = "SEROK";

	@XmlElement(name = "IP")
	private String IP = "";
	@XmlElement(name = "port")
	private String port = "";

	@XmlElement(name = "hops")
	private int hops = 0;

	@XmlElement(name = "noFiles")
	private int noFiles = 0;

	@XmlElementWrapper(name = "files")
	@XmlElement(name = "file")
	private ArrayList<String> fileNames;

	public SearchResponseBean() {
	}

	public String getMessage() {
		return message;
	}

	public int getNoFiles() {
		return noFiles;
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

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setNoFiles(int noFiles) {
		this.noFiles = noFiles;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setHops(int hopes) {
		this.hops = hopes;
	}

	public void setFileNames(ArrayList<String> fileNames) {
		this.fileNames = fileNames;
	}
}
