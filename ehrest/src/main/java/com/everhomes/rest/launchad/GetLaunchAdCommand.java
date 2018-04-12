package com.everhomes.rest.launchad;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>appId: appId</li>
 *     <li>currentOrgId: 物业公司id</li>
 * </ul>
 */
public class GetLaunchAdCommand {

    @NotNull
    private Integer namespaceId;
    private Long appId;
    private Long currentOrgId;

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
