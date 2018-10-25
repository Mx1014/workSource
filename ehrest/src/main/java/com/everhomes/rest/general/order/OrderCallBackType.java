package com.everhomes.rest.general.order;

import com.everhomes.util.StringHelper;

/**
 * @author 黄明波
 *
 */
public enum OrderCallBackType {

	ENTERPRISE_PAY((byte)0, "企业支付"), 
	INVOICE((byte)1,  "发票");
	
	private Byte code;
	private String info; // 说明

	private OrderCallBackType(Byte code, String info) {
		this.code = code;
		this.info = info;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getCode() {
		return code;
	}

	public void setCode(Byte code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
