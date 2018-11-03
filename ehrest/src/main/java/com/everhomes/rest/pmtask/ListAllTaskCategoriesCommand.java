package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

public class ListAllTaskCategoriesCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;

	private Long appId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
