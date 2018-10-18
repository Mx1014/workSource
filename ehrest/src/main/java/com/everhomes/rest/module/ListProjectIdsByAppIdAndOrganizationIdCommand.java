package com.everhomes.rest.module;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId：域空间id </li>
 * <li>appId：应用id </li>
 * <li>orgId：公司id </li>
 * </ul>
 */

public class ListProjectIdsByAppIdAndOrganizationIdCommand {
    private Integer namespaceId;
    private Long appId;
    private Long orgId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

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
