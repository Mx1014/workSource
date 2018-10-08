package com.everhomes.rest.investmentAd;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>contentUri：附件uri</li>
 * 	<li>contentUrl：附件url </li>
 * </ul>
 */
public class InvestmentAdBannerDTO {
	
	private String contentUri;
	private String contentUrl;
	
	public String getContentUri() {
		return contentUri;
	}
	
	public void setContentUri(String contentUri) {
		this.contentUri = contentUri;
	}
	
	public String getContentUrl() {
		return contentUrl;
	}
	
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
