package com.everhomes.rest.community_approve;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId </li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations {@link com.everhomes.entity.EntityType}</li>
 * <li>organizationId: 属于的公司 ID</li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 * <li>approveName:审批名称</li>
 * <li>status:启用状态 1:不启用 2:启用</li>
 * <li>formOriginId:表单id</li>
 * <li>formVersion:表单版本</li>
 * </ul>
 *
 */
public class CommunityApproveDTO {

    private Long id;
    private Long     ownerId;
    private String     ownerType;
    private Long     moduleId;
    private String     moduleType;
    private Long     organizationId;
    private String approveName;
    private Byte status;
    private Long formOriginId;
    private Integer formVersion;
    private String formName;
    private String flowName;
    private Timestamp updateTime;
    private Integer     namespaceId;
    private Timestamp     createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Integer getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Integer formVersion) {
        this.formVersion = formVersion;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}
