package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 域下的字段id，新加进去的没有</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>moduleName: 字段所属的模块类型名</li>
 *     <li>fieldId: 在系统里的字段id</li>
 *     <li>itemId: 在系统里的字段选项id</li>
 *     <li>itemDisplayName: 字段选项名</li>
 *     <li>defaultOrder: 顺序</li>
 *     <li>businessValue: 在业务模块中定义的值，没有则为空</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ScopeFieldItemInfo {

    private Long id;

    private Integer namespaceId;

    private String moduleName;

    private Long fieldId;

    private Long itemId;

    private String itemDisplayName;

    private Integer defaultOrder;

    private Byte businessValue;
    
	private Long categoryId;

	private Long ownerId;

	private String ownerType;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

    public Byte getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(Byte businessValue) {
        this.businessValue = businessValue;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemDisplayName() {
        return itemDisplayName;
    }

    public void setItemDisplayName(String itemDisplayName) {
        this.itemDisplayName = itemDisplayName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
