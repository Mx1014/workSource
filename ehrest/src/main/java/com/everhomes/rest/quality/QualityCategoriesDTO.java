package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>id: category表的主键id</li>
 *  <li>parentId: 父类型在category表的主键id</li>
 *  <li>name: 类型名称</li>
 *  <li>ownerId: 类型所属的主体id</li>
 *  <li>ownerType: 类型所属的主体，如enterprise</li>
 *  <li>path: 类型路径</li>
 *  <li>status: 类型状态 参考 com.everhomes.rest.quality.QualityInspectionCategoryStatus</li>
 *  <li>defaultOrder: </li>
 * </ul>
 */
public class QualityCategoriesDTO {

	private Long id;
	
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
