package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId </li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations</li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 * <li>organizationId: 属于的公司 ID</li>
 * <li>projectType : </li>
 * <li>projectId : </li>
 * <li>supportType: APP可用，WEB 可用，APP 与 WEB 都可用 {@link com.everhomes.rest.general_approval.GeneralApprovalSupportType}</li>
 * </ul>
 */
public class CreateApprovalTemplatesCommand {

    private Long     ownerId;
    private String     ownerType;
    private Long     moduleId;
    private String     moduleType;
    private Long organizationId;
    private String projectType;
    private Long projectId;
    private Byte supportType;

    public CreateApprovalTemplatesCommand() {
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Byte getSupportType() {
        return supportType;
    }

    public void setSupportType(Byte supportType) {
        this.supportType = supportType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
