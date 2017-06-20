// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>logonURL : 登录的地址</li>
 * <li>identifierToken : url后跟的唯一标识</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintLogonUrlResponse {
	private String logonURL;
	private Long identifierToken;
	public String getLogonURL() {
		return logonURL;
	}
	public void setLogonURL(String logonURL) {
		this.logonURL = logonURL;
	}
	public Long getIdentifierToken() {
		return identifierToken;
	}
	public void setIdentifierToken(Long identifierToken) {
		this.identifierToken = identifierToken;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
