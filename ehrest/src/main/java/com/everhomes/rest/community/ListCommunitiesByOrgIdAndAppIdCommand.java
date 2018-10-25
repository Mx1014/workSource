package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>orgId: 公司Id</li>
 *     <li>appId: 应用Id，标准版必传，定制版可不传</li>
 * </ul>
 */
public class ListCommunitiesByOrgIdAndAppIdCommand {

	@NotNull
	private Long orgId;

	private Long appId;


	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
