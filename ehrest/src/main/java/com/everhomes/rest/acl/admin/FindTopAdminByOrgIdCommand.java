package com.everhomes.rest.acl.admin;

import com.everhomes.util.StringHelper;

public class FindTopAdminByOrgIdCommand {
	Long orgId;

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
