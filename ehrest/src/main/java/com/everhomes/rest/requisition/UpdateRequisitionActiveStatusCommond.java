package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId:namespaceId</li>
 *     <li>id : 想要修改状态的审批ID</li>
 *     <li>ownerId : 想要修改的项目，填入communityId</li>
 *     <li>moduleId : moduleId</li>
 *     <li>status : 想要修改的状态</li>
 * </ul>
 */
public class UpdateRequisitionActiveStatusCommond {
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private Long moduleId;
    private Long id;
    private Long orgId;
    private Byte status;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
