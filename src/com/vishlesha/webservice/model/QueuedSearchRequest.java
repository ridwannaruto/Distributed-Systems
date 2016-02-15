package com.vishlesha.webservice.model;

import com.vishlesha.app.GlobalState;

import javax.xml.bind.annotation.XmlElement;
import java.util.Vector;

/**
 * Created by tharindu on 2/9/16.
 */
public class QueuedSearchRequest {

	private String initiator = "";
	private String query = "SER";

	private Vector<String> out=new Vector<String>();
	private String in="";

	private int index=-1;

	public QueuedSearchRequest() {

	}

	public void setIndex(final int i) {
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							SearchContext.processingRequest.remove(i);
						}catch (Exception e)
						{

						}
					}
				},
				2000
		);
	}

	public String getInitiator() {
		return initiator;
	}


	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Vector<String> getOut() {
		return out;
	}

	public void setOut(Vector<String> out) {
		this.out = out;
	}

	public String getIn() {
		return in;
	}

	public void setIn(String in) {
		this.in = in;
	}


	@Override public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof QueuedSearchRequest) {
			QueuedSearchRequest that = (QueuedSearchRequest) other;
			result = (this.getInitiator().equals(that.getInitiator()) && this.getQuery().equals(that.getQuery()));
		}
		return result;
	}


}
