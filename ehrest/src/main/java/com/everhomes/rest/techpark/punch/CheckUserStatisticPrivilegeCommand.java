package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orgId: 公司id</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class CheckUserStatisticPrivilegeCommand {
    private Long orgId;
    private Long appId;
    public CheckUserStatisticPrivilegeCommand() {
        super();
    }

    public CheckUserStatisticPrivilegeCommand(Long orgId) {
        super();
        this.orgId = orgId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

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
}
