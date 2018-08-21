package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>organizationId: 属于的公司 ID</li>
 *     <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId</li>
 *     <li>ownerType: 对象类型</li>
 *     <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 *     <li>moduleType: 模块类型 默认"any-module" </li>
 *     <li>sourceId: sourceId</li>
 *     <li>sourceType: sourceType</li>
 *     <li>formOriginId: 原始 formId，如果修改了版本，则原始的数据保留</li>
 *     <li>formVersion: 版本 </li>
 *     <li>fieldName: 字段名称</li>
 *     <li>fieldType: 字段类型</li>
 *     <li>fieldValue: 字段值</li>
 *     <li>valExtra: 自定义数据, json 字符串</li>
 * </ul>
 */
public class GeneralFormValDTO {

    private Long id;
    private Integer namespaceId;
    private Long organizationId;
    private Long ownerId;
    private String ownerType;
    private Long moduleId;
    private String moduleType;
    private Long sourceId;
    private String sourceType;
    private Long formOriginId;
    private Long formVersion;
    private String fieldName;
    private String fieldType;
    private String fieldValue;

    private String valExtra;

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

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getValExtra() {
        return valExtra;
    }

    public void setValExtra(String valExtra) {
        this.valExtra = valExtra;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

