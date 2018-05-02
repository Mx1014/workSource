package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>originId: originId</li>
 *     <li>appName: appName</li>
 * </ul>
 */
public class ListFlowModuleAppServiceTypesCommand {

    private Integer namespaceId;
    private Long moduleId;
    private Long originId;
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

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
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
