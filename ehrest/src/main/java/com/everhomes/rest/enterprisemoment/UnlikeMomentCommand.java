// @formatter:off
package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>enterpriseMomentId: 动态id</li>
 * </ul>
 */
public class UnlikeMomentCommand {

	private Long organizationId;

	private Long enterpriseMomentId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
 
	public Long getEnterpriseMomentId() {
		return enterpriseMomentId;
	}

	public void setEnterpriseMomentId(Long enterpriseMomentId) {
		this.enterpriseMomentId = enterpriseMomentId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
