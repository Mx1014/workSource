package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>moduleType: moduleType</li>
 *     <li>ownerId: 填入approvalId</li>
 *     <li>ownerType: 填入‘GENERAL_APPROVAL’</li>
 *     <li>projectId: 填入communityId</li>
 *     <li>projectType: 填入‘EhCommunities’</li>
 * </ul>
 */
public class GetRunningRequisitionFlowCommand {
    private Integer namespaceId;
    private Long moduleId;
    private String moduleType;
    private Long ownerId;
    private String ownerType;
    private Long projectId;
    private String projectType;
    private Long orgId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
