package com.everhomes.rest.wanke;

/**
 * <ul>
 * <li>appId: 应用id</li>
 * <li>timestamp: 时间戳</li>
 * <li>nonceStr: 随机数</li>
 * <li>signature: 签名</li>
 * <li>url: 当前页面url</li>
 * </ul>
 */
public class GetSignDTO {
	private String appId;
	private String timestamp;
	private String nonceStr;
	private String signature;
	private String url;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
