// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>logonURL : 登录的地址</li>
 * <li>identifierToken : url后跟的唯一标识</li>
 * <li>type : pc:驱动, printer，打印机</li>
 * <li>scanTimes : 扫描scanTimes次，二维码刷新</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintLogonUrlResponse {
	private String logonURL;
	private String identifierToken;
	private String type;
	private Integer scanTimes;
	private String base64;
	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
