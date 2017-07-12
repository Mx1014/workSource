package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: 模板字段id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>ownerId：账单所属物业公司id</li>
 *     <li>ownerType：账单模板字段所属物业公司类型</li>
 *     <li>targetId：账单模板字段所属园区id</li>
 *     <li>targetType：账单模板字段所属园区类型</li>
 *     <li>fieldDisplayName:字段展示名</li>
 *     <li>fieldName:字段对应数据库字段名</li>
 *     <li>fieldType:字段类型 参考{@link com.everhomes.rest.asset.FieldType}</li>
 *     <li>fieldCustomName:用户重命名字段值</li>
 *     <li>requiredFlag:能否禁用</li>
 *     <li>selectedFlag:是否启用</li>
 *     <li>defaultOrder:顺序</li>
 *     <li>templateVersion: 模板字段版本号</li>
 * </ul>
 */
public class AssetBillTemplateFieldDTO {

    private Long id;

    private Integer namespaceId;

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    @NotNull
    private Long targetId;

    @NotNull
    private String targetType;

    private String fieldDisplayName;

    private String fieldName;

    private String fieldType;

    private String fieldCustomName;

    private Byte requiredFlag;

    private Byte selectedFlag;

    private Integer defaultOrder;

    private Long templateVersion;

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

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
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

    public String getFieldCustomName() {
        return fieldCustomName;
    }

    public void setFieldCustomName(String fieldCustomName) {
        this.fieldCustomName = fieldCustomName;
    }

    public Byte getRequiredFlag() {
        return requiredFlag;
    }

    public void setRequiredFlag(Byte requiredFlag) {
        this.requiredFlag = requiredFlag;
    }

    public Byte getSelectedFlag() {
        return selectedFlag;
    }

    public void setSelectedFlag(Byte selectedFlag) {
        this.selectedFlag = selectedFlag;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Long getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(Long templateVersion) {
        this.templateVersion = templateVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
