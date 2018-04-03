package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class ListUserRelatedOrganizationAddressesCommand {

	@NotNull
	private Long userId;

	private Integer namespaceId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
