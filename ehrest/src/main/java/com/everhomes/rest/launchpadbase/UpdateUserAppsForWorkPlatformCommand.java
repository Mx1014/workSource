package com.everhomes.rest.launchpadbase;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 企业id</li>
 *     <li>appIds: 按照顺序排列顺序传来appId</li>
 * </ul>
 */
public class UpdateUserAppsForWorkPlatformCommand {

    private Long organizationId;

    private List<Long> appIds;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<Long> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<Long> appIds) {
        this.appIds = appIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
