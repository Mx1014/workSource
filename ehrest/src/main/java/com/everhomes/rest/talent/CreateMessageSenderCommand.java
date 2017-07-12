// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType: 所属者类型</li>
 * <li>ownerId: 所属者id</li>
 * <li>organizationId: 组织id</li>
 * <li>organizationMemberId: 组织成员id</li>
 * <li>userId: 用户id，对应于targetId</li>
 * </ul>
 */
public class CreateMessageSenderCommand {	
	@NotNull
	@Size(min=1)
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long organizationId;
	@NotNull
	private Long organizationMemberId;
	@NotNull
	private Long userId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
