package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class GetContactInfoByUserIdCommand {

	private Long userId;

	private Long organizationId;

	public GetContactInfoByUserIdCommand() {
	}

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
