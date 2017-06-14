// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>organizationMemberId: 公司人员表id</li>
 * <li>userId: 用户id</li>
 * </ul>
 */
public class CreateExpressUserDTO {
	private Long organizationId;
	private Long organizationMemberId;
	private Long userId;

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

	public Long getOrganizationMemberId() {
		return organizationMemberId;
	}

	public void setOrganizationMemberId(Long organizationMemberId) {
		this.organizationMemberId = organizationMemberId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
