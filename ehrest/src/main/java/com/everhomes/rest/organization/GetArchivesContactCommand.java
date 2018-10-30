package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class GetArchivesContactCommand {
	private Long userId;
	private Long organizationId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
}
