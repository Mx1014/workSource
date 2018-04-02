package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>appId: 应用Id</li>
 *     <li>orgId: 公司Id</li>
 * </ul>
 */
public class InstallAppCommand {
	private Long appId;
	private Long orgId;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
