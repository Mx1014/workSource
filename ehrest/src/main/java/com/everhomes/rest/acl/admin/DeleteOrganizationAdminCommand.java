package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>organizationId: 目标机构id</li>
 * <li>currentOrganizationId: 当前公司的id</li>
 * <li>contactToken: 管理员手机号</li>
 * <li>communityId: 小区id</li>
 * <li>namespaceId:namespaceId </li>
 * <li>customerId:  customer organization之间的映射关系</li>
 * </ul>
 */
public class DeleteOrganizationAdminCommand {

	@NotNull
	private String ownerType;

	@NotNull
	private Long ownerId;

	@NotNull
	private Long organizationId;
	private Long currentOrganizationId;

	@NotNull
	private String contactToken;

	private Long userId;

	private  Long communityId;

	private Integer namespaceId;

	private Long customerId;

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


	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getCurrentOrganizationId() {
		return currentOrganizationId;
	}

	public void setCurrentOrganizationId(Long currentOrganizationId) {
		this.currentOrganizationId = currentOrganizationId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
