package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>originId: 应用originId</li>
 * </ul>
 */
public class InstallAppForAllOrganizationsCommand {
	private Long originId;

	public Long getOriginId() {
		return originId;
	}

	public void setOriginId(Long originId) {
		this.originId = originId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
