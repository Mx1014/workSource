package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId : namespaceId</li>
 *     <li>moduleId : 模块ID</li>
 *     <li>moduleType : moduleType</li>
 *     <li>ownerId : ownerId</li>
 *     <li>ownerType : ownerType</li>
 * </ul>
 */
public class GetRunningRequisitionFormCommond {
    private Integer namespaceId;
    private Long moduleId;
    private String moduleType;
    private Long ownerId;
    private String ownerType;

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

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
