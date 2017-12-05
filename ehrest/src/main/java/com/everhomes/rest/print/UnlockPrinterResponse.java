// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

public class UnlockPrinterResponse {
	private String url;
	private String params;
	
	public UnlockPrinterResponse() {
	}
	public UnlockPrinterResponse(String url, String params) {
		super();
		this.url = url;
		this.params = params;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
