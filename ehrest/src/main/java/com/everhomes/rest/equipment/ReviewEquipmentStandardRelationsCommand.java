package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 设备-标准关联id</li>
 *  <li>equipmentId: 设备id</li>
 *  <li>standardId: 标准id</li>
 *  <li>reviewResult: 审批结果 参考{@link com.everhomes.rest.equipment.ReviewResult}</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * </ul>
 */
public class ReviewEquipmentStandardRelationsCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;
	@NotNull
	private Long id;
	@NotNull
	private Long equipmentId;
	@NotNull
	private Long standardId;
	@NotNull
	private Byte reviewResult;

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
