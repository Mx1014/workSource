package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <p>参数:</p>
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class CheckAdminCommand {
    private Long organizationId;
    private Long appId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
