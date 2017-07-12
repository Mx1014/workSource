package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 所属的主体id</li>
 *  <li>ownerType: 所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 所属管理处id</li>
 *  <li>targetType: 所属管理处类型</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 * </ul>
 */
public class ImportOwnerCommand {
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Long targetId;
	
	private String targetType;
	
	private Long inspectionCategoryId;
	
	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
