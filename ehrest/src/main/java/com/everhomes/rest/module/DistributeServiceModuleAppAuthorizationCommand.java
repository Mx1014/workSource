package com.everhomes.rest.module;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId：域空间id </li>
 * <li>appId：应用id </li>
 * <li>fromOrgId：原从属公司 </li>
 * <li>toOrgId：分配公司 </li>
 * <li>projectIds：项目id集合 </li>
 * </ul>
 */

public class DistributeServiceModuleAppAuthorizationCommand {
    private Integer namespaceId;
    private Long appId;
    private Long fromOrgId;
    private Long toOrgId;
    private List<Long> projectIds;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

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

    public Long getFromOrgId() {
        return fromOrgId;
    }

    public void setFromOrgId(Long fromOrgId) {
        this.fromOrgId = fromOrgId;
    }

    public Long getToOrgId() {
        return toOrgId;
    }

    public void setToOrgId(Long toOrgId) {
        this.toOrgId = toOrgId;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projectIds) {
        this.projectIds = projectIds;
    }
}
