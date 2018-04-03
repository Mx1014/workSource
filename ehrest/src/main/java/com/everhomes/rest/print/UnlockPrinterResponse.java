// @formatter:off
package com.everhomes.rest.print;

import java.util.Map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class UnlockPrinterResponse {
	private String url;
	@ItemType(String.class)
	private Map<String,String> params;
	
	public UnlockPrinterResponse(String url, Map<String, String> params) {
		super();
		this.url = url;
		this.params = params;
	}
	public UnlockPrinterResponse() {
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
