package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>payNo: 支付单号</li>
 *     <li>appId: 微信appId</li>
 *     <li>timestamp: 时间戳</li>
 *     <li>nonceStr: 随机数</li>
 *     <li>packg: packg</li>
 *     <li>signType: 签名类型,如:MD5</li>
 *     <li>paySign: 签名</li>
 * </ul>
 */
public class CreateWechatJsPayOrderResp {
	private String payNo;
	private String appId;
	private String timestamp;
	private String nonceStr;
	private String packg;
	private String signType;
	private String paySign;

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

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

	public String getPackg() {
		return packg;
	}

	public void setPackg(String packg) {
		this.packg = packg;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
