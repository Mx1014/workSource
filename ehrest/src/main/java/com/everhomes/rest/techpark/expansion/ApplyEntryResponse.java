package com.everhomes.rest.techpark.expansion;

/**
 * <ul> 
 * <li>url：客户端跳转的zl链接</li> 
 * </ul>
 */
public class ApplyEntryResponse {
	private String url;

	public ApplyEntryResponse() {
		super();
	}

	public ApplyEntryResponse(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
