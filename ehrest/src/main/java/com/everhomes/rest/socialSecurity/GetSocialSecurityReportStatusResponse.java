// @formatter:off
package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>status:  1-OK, 0-不OK</li>
 * </ul>
 */
public class GetSocialSecurityReportStatusResponse {

	private Byte status;

	public GetSocialSecurityReportStatusResponse() {

	}

	public GetSocialSecurityReportStatusResponse(Byte status) {
		super();
		this.status = status;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
