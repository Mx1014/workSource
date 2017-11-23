package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>organizationId: 机构id</li>
 * <li>contactToken: 管理员手机号</li>
 * </ul>
 */
public class DeleteOrganizationAdminCommand {

	@NotNull
	private String ownerType;

	@NotNull
	private Long ownerId;

	@NotNull
	private Long organizationId;

	@NotNull
	private String contactToken;

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

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
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
