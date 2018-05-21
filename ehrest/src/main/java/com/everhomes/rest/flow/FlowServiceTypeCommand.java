package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>appId: appId</li>
 *     <li>serviceName: serviceName</li>
 * </ul>
 */
public class FlowServiceTypeCommand {

    private Integer namespaceId;
    private Long moduleId;
    private Long appId;
    private String serviceName;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
