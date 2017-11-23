package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>timestamp: 生成签名的时间戳</li>
 *  <li>nonceStr: 生成签名的随机串</li>
 *  <li>signature: 签名</li>
 *  <li>ticket: jsapi ticket</li>
 *  <li>appId: appId ticket</li>
 * </ul>
 *
 */
public class GetSignatureResponse {
	
	private String timestamp;
	
	private String nonceStr;
	
	private String signature;
	
	private String ticket;

	private String appId;
	
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

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
