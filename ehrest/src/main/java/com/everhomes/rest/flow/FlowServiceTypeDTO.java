package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>moduleId: moduleId</li>
 *     <li>organizationId: organizationId</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>serviceName: serviceName</li>
 * </ul>
 */
public class FlowServiceTypeDTO implements Serializable {



    private Long id;
    private Long moduleId;
    private Long organizationId;
    private Integer namespaceId;
    private String serviceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
