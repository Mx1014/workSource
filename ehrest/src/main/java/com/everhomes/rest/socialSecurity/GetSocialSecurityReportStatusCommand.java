// @formatter:off
package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型 organization</li>
 * <li>ownerId: 所属id 公司id</li>
 * <li>reportToken: 计算报表返回的唯一标识</li>
 * </ul>
 */
public class GetSocialSecurityReportStatusCommand {

	private String ownerType;

	private Long ownerId;

	private String reportToken;

	public GetSocialSecurityReportStatusCommand() {

	}

	public GetSocialSecurityReportStatusCommand(String ownerType, Long ownerId, String reportToken) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.reportToken = reportToken;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getReportToken() {
		return reportToken;
	}

	public void setReportToken(String reportToken) {
		this.reportToken = reportToken;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
