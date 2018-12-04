package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域ID</li>
 *     <li>organizationId: 属于的公司 ID</li>
 *     <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId</li>
 *     <li>ownerType: 对象类型，默认为 EhOrganizations </li>
 *     <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 *     <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 *     <li>projectId: 项目ID</li>
 *     <li>projectType: 项目类型</li>
 *     <li>formOriginId: 表单原始ID</li>
 *     <li>formVersion: 表单版本</li>
 *     <li>configType：配置类型，默认"flowNode-visible" {@link GeneralFormFieldsConfigType}</li>
 *     <li>formFields：表单字段 {@link GeneralFormFieldsConfigFieldDTO}</li>
 * </ul>
 * @author huqi
 */
public class CreateFormFieldsConfigCommand {
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
    private String configType;
    private List<GeneralFormFieldsConfigFieldDTO> formFields;

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
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

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public List<GeneralFormFieldsConfigFieldDTO> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GeneralFormFieldsConfigFieldDTO> formFields) {
        this.formFields = formFields;
    }
}
