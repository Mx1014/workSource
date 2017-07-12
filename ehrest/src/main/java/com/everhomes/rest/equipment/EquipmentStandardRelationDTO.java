package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *  <li>equipmentId: 设备id</li>
 *  <li>targetId: 标准所属管理处id</li>
 *  <li>targetName: 标准所属管理处名</li>
 *  <li>equipmentName: 设备名称</li>
 *  <li>equipmentModel: 设备型号</li>
 *  <li>qrCodeFlag: 二维码状态</li>
 *  <li>status: 设备状态 参考{@link com.everhomes.rest.equipment.EquipmentStatus}</li>
 *  <li>standardId: 关联标准id</li>
 *  <li>standardName: 标准名称</li>
 *  <li>reviewStatus: 审批状态 参考{@link com.everhomes.rest.equipment.EquipmentReviewStatus}</li>
 *  <li>reviewResult: 审批结果 参考{@link com.everhomes.rest.equipment.ReviewResult}</li>
 *  <li>reviewer: 审批人</li>
 *  <li>reviewTime: 审批时间</li>
 * </ul>
 */
public class EquipmentStandardRelationDTO {

	private Long id;
	
	private Long equipmentId;
	
	private Long targetId;
	
	private String targetName;
	
	private String equipmentName;
	
	private String equipmentModel;
	
	private Byte qrCodeFlag;
	
	private Byte status;
	
	private Long standardId;
	
	private String standardName;
	
	private Byte reviewStatus;
	
	private Byte reviewResult;

	private String reviewer;

	private Timestamp reviewTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentModel() {
		return equipmentModel;
	}

	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
	}


	public Byte getQrCodeFlag() {
		return qrCodeFlag;
	}

	public void setQrCodeFlag(Byte qrCodeFlag) {
		this.qrCodeFlag = qrCodeFlag;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public Timestamp getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Timestamp reviewTime) {
		this.reviewTime = reviewTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
