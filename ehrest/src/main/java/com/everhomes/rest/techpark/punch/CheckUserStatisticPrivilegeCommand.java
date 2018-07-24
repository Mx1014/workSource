package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orgId: 公司id</li>
 * </ul>
 */
public class CheckUserStatisticPrivilegeCommand {
    private Long orgId;

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
}
