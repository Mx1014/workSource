package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>userId: 用户Id，不传则为当前用户</li>
 *     <li>projectId: 项目Id</li>
 *     <li>appId: appId，根据应用id判断公司是否管理项目</li>
 * </ul>
 */
public class ListUserOrganizationsCommand {
    private Long userId;

    private Long projectId;

    private Long appId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
