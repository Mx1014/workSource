package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>appId: appId</li>
 *     <li>appName: appName</li>
 * </ul>
 */
public class FlowModuleAppDTO {

    private Integer namespaceId;
    private Long moduleId;
    private Long appId;
    private String appName;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {

        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
