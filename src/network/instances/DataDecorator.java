package network.instances;

import java.io.Serializable;
import java.util.Deque;

import network.deprecated.RequestType;
import network.framework.format.Mail;

public class DataDecorator implements Mail {
	
	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = -6320582607260270478L;
	
	RequestType request;
	
	Serializable data;
	Deque<String> anscestralPath;

	public DataDecorator (RequestType r, Serializable data, Deque<String> path) {
		this.request = r;
		
		this.data = data;
		this.anscestralPath = path;
	}
	
	@Override
	public RequestType getRequest() {
		return request;
	}

	@Override
	public Serializable getData() {
		return data;
	}

	@Override
	public Deque<String> getPath() {
		return anscestralPath;
	}

}
