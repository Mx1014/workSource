package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 域下的字段id，新加进去的没有</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>moduleName: 字段所属的模块类型名</li>
 *     <li>fieldId: 在系统里的字段id</li>
 *     <li>fieldDisplayName: 字段名</li>
 *     <li>fieldParam: 字段描述，json 如：{fieldParamType: "file", length: 9}，字段类型枚举型，参考{@link com.everhomes.rest.varField.FieldParamType}</li>
 *     <li>mandatoryFlag: 是否必填 0: 否; 1: 是</li>
 *     <li>defaultOrder: 顺序</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ScopeFieldInfo {

    private Long id;

    private Integer namespaceId;

    private Long communityId;

    private String moduleName;

    private Long fieldId;

    private String fieldDisplayName;

    private String fieldParam;

    private Byte mandatoryFlag;

    private Integer defaultOrder;

    private String groupPath;

    private Long groupId;
    
    private Long categoryId;

    private Long ownerId;

    private String ownerType;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupPath() {
        return groupPath;
    }

    public void setGroupPath(String groupPath) {
        this.groupPath = groupPath;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldParam() {
        return fieldParam;
    }

    public void setFieldParam(String fieldParam) {
        this.fieldParam = fieldParam;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getMandatoryFlag() {
        return mandatoryFlag;
    }

    public void setMandatoryFlag(Byte mandatoryFlag) {
        this.mandatoryFlag = mandatoryFlag;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
