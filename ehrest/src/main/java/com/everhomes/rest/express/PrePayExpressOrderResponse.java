// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <ul>参数:
 * <li>appid: appid</li>
 * <li>partnerid: 商户id</li>
 * <li>prepayid: 预支付id</li>
 * <li>package: package</li>
 * <li>noncestr: 随机数</li>
 * <li>timestamp: 时间搓</li>
 * <li>sign: 签名</li>
 * </ul>
 */
public class PrePayExpressOrderResponse {
	private String appid;
	private String partnerid;
	private String prepayid;
	@JsonProperty("package")
	private String packagetype;
	private String noncestr;
	private String timestamp;
	private String sign;
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getPackagetype() {
		return packagetype;
	}

	public void setPackagetype(String packagetype) {
		this.packagetype = packagetype;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
