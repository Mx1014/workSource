// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>organizationMemberId: 组织成员id</li>
 * <li>userId: 用户id，对应于targetId</li>
 * </ul>
 */
public class CreateMessageSenderDTO {
	@NotNull
	private Long organizationMemberId;
	@NotNull
	private Long userId;

	public Long getOrganizationMemberId() {
		return organizationMemberId;
	}

	public void setOrganizationMemberId(Long organizationMemberId) {
		this.organizationMemberId = organizationMemberId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
