package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 域下的组id，新加进去的没有</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>moduleName: 组所属的模块类型名</li>
 *     <li>groupId: 在系统组里的id</li>
 *     <li>groupDisplayName: 组名</li>
 *     <li>defaultOrder: 顺序</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ScopeFieldGroupInfo {

    private Long id;

    private Integer namespaceId;

    private String moduleName;

    private Long groupId;

    private String groupDisplayName;

    private Integer defaultOrder;
    
	private Long categoryId;

    private Long ownerId;

    private String ownerType;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getGroupDisplayName() {
        return groupDisplayName;
    }

    public void setGroupDisplayName(String groupDisplayName) {
        this.groupDisplayName = groupDisplayName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
