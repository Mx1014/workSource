// @formatter:off
package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>reportToken: 查询计算结果的token参数</li>
 * </ul>
 */
public class CalculateSocialSecurityReportsResponse {


	private String reportToken;

	public CalculateSocialSecurityReportsResponse() {

	}
 
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getReportToken() {
		return reportToken;
	}

	public void setReportToken(String reportToken) {
		this.reportToken = reportToken;
	}

}
