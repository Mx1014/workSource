package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: id</li>
 *  <li>standardId: 标准id</li>
 *  <li>standardName: 标准名称</li>
 *  <li>equipmentId: 设备id</li>
 *  <li>reviewStatus: 审批状态 参考{@link com.everhomes.rest.equipment.EquipmentReviewStatus}</li>
 *  <li>reviewResult: 审批结果 参考{@link com.everhomes.rest.equipment.ReviewResult}</li>
 *  <li>reviewerUid: 审批人id</li>
 *  <li>reviewerName: 审批人姓名</li>
 *  <li>reviewTime: 审批时间</li>
 * </ul>
 */
public class EquipmentStandardMapDTO {

	private Long id;
	
	private Long standardId;
	
	private String standardName;
	
	private Long equipmentId;
	
	private Byte reviewStatus;
	
	private Byte reviewResult;
	
	private Long reviewerUid;
	
	private String reviewerName;
	
	private Long reviewTime;

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

	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Byte getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Byte reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}

	public Long getReviewerUid() {
		return reviewerUid;
	}

	public void setReviewerUid(Long reviewerUid) {
		this.reviewerUid = reviewerUid;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public Long getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Long reviewTime) {
		this.reviewTime = reviewTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
