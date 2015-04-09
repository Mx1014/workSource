package com.everhomes.sms;

import org.apache.commons.collections.MultiMap;

public class RspMessage {
	private String message;

	private MultiMap headers;

	public RspMessage(String message, MultiMap headers) {
		super();
		this.message = message;
		this.headers = headers;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MultiMap getHeaders() {
		return headers;
	}

	public void setHeaders(MultiMap headers) {
		this.headers = headers;
	}

}
