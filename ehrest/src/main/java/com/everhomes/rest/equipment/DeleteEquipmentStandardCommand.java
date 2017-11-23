package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>standardId: 标准id</li>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 标准所属的项目id</li>
 *  <li>targetType: 标准所属的项目类型</li>
 * </ul>
 */
public class DeleteEquipmentStandardCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long standardId;

	private String targetType;

	private Long targetId;

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

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
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
