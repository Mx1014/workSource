// @formatter:off
package com.everhomes.express.guomao;

import com.everhomes.util.StringHelper;

public class GuoMaoEMSLogisticsItem {
	private String acceptTime;
	private String acceptAddress;
	private String remark;
	private String code;
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	public String getAcceptAddress() {
		return acceptAddress;
	}
	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
