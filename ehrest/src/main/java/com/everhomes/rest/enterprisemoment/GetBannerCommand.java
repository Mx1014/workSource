package com.everhomes.rest.enterprisemoment;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class GetBannerCommand {
	private Long organizationId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
}
