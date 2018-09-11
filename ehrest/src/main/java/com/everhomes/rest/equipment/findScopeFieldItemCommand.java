package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId : 域空间id</li>
 * <li>communityId : 项目id</li>
 * <li>moduleName : equipment_inspction</li>
 * <li>businessValue : 业务枚举 byte</li>
 * </ul>
 */
public class findScopeFieldItemCommand {

    private Integer namespaceId;

    private Long ownerId;

    private String ownerType;

    private Long communityId;

    private String moduleName;

    private Long fieldId;

    private Byte businessValue;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Byte getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(Byte businessValue) {
        this.businessValue = businessValue;
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
