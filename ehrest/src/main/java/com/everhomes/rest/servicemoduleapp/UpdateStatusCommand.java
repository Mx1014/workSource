package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>status: 状态 {@link com.everhomes.rest.servicemoduleapp.OrganizationAppStatus}</li>
 *     <li>orgAppId: orgAppId</li>
 * </ul>
 */
public class UpdateStatusCommand {
	private Byte status;

	private Long orgAppId;

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

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
