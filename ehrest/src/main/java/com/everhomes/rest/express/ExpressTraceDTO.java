// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>acceptTime: 接收时间</li>
 * <li>acceptAddress: 接收地址</li>
 * <li>remark: 备注</li>
 * </ul>
 */
public class ExpressTraceDTO {
	private String acceptTime;
	private String acceptAddress;
	private String remark;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
