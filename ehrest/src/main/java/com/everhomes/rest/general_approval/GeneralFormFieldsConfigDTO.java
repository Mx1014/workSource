package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id：表单字段配置ID</li>
 *     <li>formOriginId: 表单原始ID</li>
 *     <li>formVersion: 表单版本</li>
 *     <li>configOriginId: 表单字段配置原始ID</li>
 *     <li>configVersion: 表单字段配置版本</li>
 *     <li>configType：配置类型，默认"flowNode-visible"</li>
 *     <li>status: 状态</li>
 *     <li>formFields：表单字段 {@link GeneralFormFieldsConfigFieldDTO}</li>
 * </ul>
 * @author huqi
 */
public class GeneralFormFieldsConfigDTO {
    private Long id;
    private Integer namespaceId;
    private Long organizationId;
    private Long ownerId;
    private String ownerType;
    private Long moduleId;
    private String moduleType;
    private Long projectId;
    private String projectType;
    private Long formOriginId;
    private Long formVersion;
    private Long configOriginId;
    private Long configVersion;
    private String configType;
    private Byte status;
    private Timestamp createTime;
    private Long creatorUid;
    private Timestamp updateTime;
    private Long updaterUid;
    private List<GeneralFormFieldsConfigFieldDTO> formFields;

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }

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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    public Long getConfigOriginId() {
        return configOriginId;
    }

    public void setConfigOriginId(Long configOriginId) {
        this.configOriginId = configOriginId;
    }

    public Long getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(Long configVersion) {
        this.configVersion = configVersion;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdaterUid() {
        return updaterUid;
    }

    public void setUpdaterUid(Long updaterUid) {
        this.updaterUid = updaterUid;
    }

    public List<GeneralFormFieldsConfigFieldDTO> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GeneralFormFieldsConfigFieldDTO> formFields) {
        this.formFields = formFields;
    }
}
