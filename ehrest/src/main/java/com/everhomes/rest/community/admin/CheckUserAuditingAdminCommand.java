// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>currentOrgId: 当前用户的组织ID</li>
 *     <li>communityId: 项目ID</li>
 * </ul>
 */
public class CheckUserAuditingAdminCommand {

    private Long currentOrgId;

    private Long communityId;

    private Long appId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(Long currentOrgId) {
        this.currentOrgId = currentOrgId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
