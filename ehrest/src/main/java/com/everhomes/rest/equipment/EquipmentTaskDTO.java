package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *  <li>id: 任务主键id</li>
 *  <li>planId: 计划id</li>
 *  <li>equipmentLocation: 设备位置</li>
 *  <li>ownerType: 任务所属机构类型 com.everhomes.rest.quality.OwnerType</li>
 *  <li>ownerId: 任务所属机构id</li>
 *  <li>taskName: 任务名称</li>
 *  <li>taskNumber: 任务编号</li>
 *  <li>executorType: 执行人类型</li>
 *  <li>executorId:执行人id</li>
 *  <li>executorName:执行人姓名</li>
 *  <li>reviewerType: 审核人类型</li>
 *  <li>reviewerId:审核人id</li>
 *  <li>reviewerName:审核人姓名</li>
 *  <li>executiveStartTime: 巡检开始时间</li>
 *  <li>executiveExpireTime: 巡检结束时间</li>
 *  <li>executiveTime: 巡检执行上报时间</li>
 *  <li>status: 执行状态 参考{@link com.everhomes.rest.equipment.EquipmentTaskStatus}</li>
 *  <li>equipments: 任务关联设备信息列表 参考{@link com.everhomes.rest.equipment.EquipmentStandardRelationDTO}</li>
 *  <li>inspectionCategoryId: 巡检对象类型</li>
 * </ul>
 */
public class EquipmentTaskDTO {
	private Long id;

	private String ownerType;
	
	private Long ownerId;

	private String targetType;

	private Long targetId;
	
	private Byte taskType;
	
	private String taskName;
	
	private String taskNumber;
	/**
	 * 兼容旧版本
	 */
	private Long equipmentId;
	/**
	 * 兼容旧版本
	 */
	private Long standardId;

	private String executorType;
	
	private Long executorId;
	
	private String executorName;

	private String operatorType;
	
	private Long operatorId;
	
	private String operatorName;
	
	private Timestamp executiveStartTime;
	
	private Timestamp executiveTime;
	
	private Timestamp executiveExpireTime;
	
	private Byte status;
	
	private Byte result;
	
	private Byte reviewResult;
	
	private Long reviewerId;
	
	private String reviewerType;
	
	private String reviewerName;

	private String planDescription;

	private Byte qrCodeFlag;

	private Byte pictureFlag;

	private String equipmentLocation;

	private String equipmentName;

	private String lastSyncTime;

	private Timestamp createTime;

	private Timestamp reviewTime;

	private Long planId;

	private Long inspectionCategoryId;

	@ItemType(EquipmentStandardRelationDTO.class)
	private List<EquipmentStandardRelationDTO> equipments;

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

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Byte getTaskType() {
		return taskType;
	}

	public void setTaskType(Byte taskType) {
		this.taskType = taskType;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	public String getExecutorType() {
		return executorType;
	}

	public void setExecutorType(String executorType) {
		this.executorType = executorType;
	}

	public Long getExecutorId() {
		return executorId;
	}

	public void setExecutorId(Long executorId) {
		this.executorId = executorId;
	}

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Timestamp getExecutiveStartTime() {
		return executiveStartTime;
	}

	public void setExecutiveStartTime(Timestamp executiveStartTime) {
		this.executiveStartTime = executiveStartTime;
	}

	public Timestamp getExecutiveTime() {
		return executiveTime;
	}

	public void setExecutiveTime(Timestamp executiveTime) {
		this.executiveTime = executiveTime;
	}

	public Timestamp getExecutiveExpireTime() {
		return executiveExpireTime;
	}

	public void setExecutiveExpireTime(Timestamp executiveExpireTime) {
		this.executiveExpireTime = executiveExpireTime;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getResult() {
		return result;
	}

	public void setResult(Byte result) {
		this.result = result;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}

	public Long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Long reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewerType() {
		return reviewerType;
	}

	public void setReviewerType(String reviewerType) {
		this.reviewerType = reviewerType;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

	public Byte getQrCodeFlag() {
		return qrCodeFlag;
	}

	public void setQrCodeFlag(Byte qrCodeFlag) {
		this.qrCodeFlag = qrCodeFlag;
	}

	public Byte getPictureFlag() {
		return pictureFlag;
	}

	public void setPictureFlag(Byte pictureFlag) {
		this.pictureFlag = pictureFlag;
	}

	public String getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(String lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Timestamp reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
	}

	public List<EquipmentStandardRelationDTO> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentStandardRelationDTO> equipments) {
		this.equipments = equipments;
	}

	public String getEquipmentLocation() {
		return equipmentLocation;
	}

	public void setEquipmentLocation(String equipmentLocation) {
		this.equipmentLocation = equipmentLocation;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
