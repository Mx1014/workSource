package com.everhomes.rest.equipment;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 类型id</li>
 *  <li>namespaceId: 类型所属域空间id</li>
 *  <li>ownerId: 类型所属组织等的id</li>
 *  <li>ownerType: 类型所属组织类型，如enterprise</li>
 *  <li>parentId: 父类型id</li>
 *  <li>name: 类型名称</li>
 *  <li>path: 类型路径</li>
 *  <li>defaultOrder: 默认顺序索引</li>
 *  <li>status: 状态，{@link com.everhomes.rest.category.CategoryAdminStatus}</li>
 * </ul>
 */
public class EquipmentInspectionCategoryDTO {

	private Long id;
	  
	private Integer namespaceId;
	  
	private String ownerType;
	  
	private Long ownerId;
	  
	private Long parentId;
	  
	private String name;
	  
	private String path;
	  
	private Integer defaultOrder;
	  
	private Byte status;

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

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
