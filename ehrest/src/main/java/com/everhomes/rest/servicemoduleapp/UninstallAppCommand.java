package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>orgAppId: 安装Id</li>
 * </ul>
 */
public class UninstallAppCommand {
	private Long orgAppId;

	public Long getOrgAppId() {
		return orgAppId;
	}

	public void setOrgAppId(Long orgAppId) {
		this.orgAppId = orgAppId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
