package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>projectId: projectId</li>
 *     <li>appId: appId</li>
 * </ul>
 */
public class GetAuthOrgByProjectIdAndAppIdCommand {
    private Long projectId;
    private Long appId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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
