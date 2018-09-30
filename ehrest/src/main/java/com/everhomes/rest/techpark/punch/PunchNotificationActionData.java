package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司ID</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class PunchNotificationActionData {
    private Long organizationId;
    private Long appId;

    public PunchNotificationActionData() {
    }

    public PunchNotificationActionData(Long organizationId) {
        this.organizationId = organizationId;
    }

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
