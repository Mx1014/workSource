// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>logonStatus : 登录反馈状态， 参考 {@link com.everhomes.rest.print.PrintLogonStatusType}</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */

public class InformPrintResponse {
	private Byte logonStatus;
	
	public InformPrintResponse() {
	}

	public InformPrintResponse(Byte logonStatus) {
		super();
		this.logonStatus = logonStatus;
	}

	public Byte getLogonStatus() {
		return logonStatus;
	}

	public void setLogonStatus(Byte logonStatus) {
		this.logonStatus = logonStatus;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
