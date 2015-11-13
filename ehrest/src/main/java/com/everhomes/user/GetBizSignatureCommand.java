package com.everhomes.user;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * 	<li>siteUri : 链接</li>
 * 	<li>siteUserToken : 第三方用户标识</li>
 * 	</ul>
 *
 */
public class GetBizSignatureCommand {
	private String siteUri;
	private String siteUserToken;
	
	public String getSiteUri() {
		return siteUri;
	}

	public void setSiteUri(String siteUri) {
		this.siteUri = siteUri;
	}

	public String getSiteUserToken() {
		return siteUserToken;
	}

	public void setSiteUserToken(String siteUserToken) {
		this.siteUserToken = siteUserToken;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
