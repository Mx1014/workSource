// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>logonURL : 登录的地址</li>
 * <li>identifierToken : url后跟的唯一标识</li>
 * <li>scanTimes : 扫描scanTimes次，二维码刷新</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintLogonUrlResponse {
	private String logonURL;
	private String identifierToken;
	private Integer scanTimes;
	public String getLogonURL() {
		return logonURL;
	}
	public void setLogonURL(String logonURL) {
		this.logonURL = logonURL;
	}
	public String getIdentifierToken() {
		return identifierToken;
	}
	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
	}
	public Integer getScanTimes() {
		return scanTimes;
	}
	public void setScanTimes(Integer scanTimes) {
		this.scanTimes = scanTimes;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
