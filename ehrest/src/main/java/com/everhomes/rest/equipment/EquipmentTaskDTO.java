package com.everhomes.rest.equipment;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 任务主键id</li>
 *  <li>standardId: 标准id</li>
 *  <li>standardName: 标准名称</li>
 *  <li>templateId: 模板id</li>
 *  <li>templateName: 模板名称</li>
 *  <li>equipmentId: 设备id</li>
 *  <li>equipmentName: 设备名称</li>
 *  <li>equipmentLocation: 设备位置</li>
 *  <li>qrCodeFlag: 二维码状态 0: inactive, 1: active</li>
 *  <li>parentId: 父任务id</li>
 *  <li>childCount: 子任务数量</li>
 *  <li>ownerType: 任务所属机构类型 com.everhomes.rest.quality.OwnerType</li>
 *  <li>ownerId: 任务所属机构id</li>
 *  <li>taskName: 任务名称</li>
 *  <li>taskNumber: 任务编号</li>
 *  <li>executiveGroupId: 执行业务组id</li>
 *  <li>groupName: 业务组名称</li>
 *  <li>executorType: 执行人类型</li>
 *  <li>executorId:执行人id</li>
 *  <li>executorName:执行人姓名</li>
 *  <li>operatorType: 维修人类型</li>
 *  <li>operatorId:维修人id</li>
 *  <li>operatorName:维修人姓名</li>
 *  <li>reviewerType: 审核人类型</li>
 *  <li>reviewerId:审核人id</li>
 *  <li>reviewerName:审核人姓名</li>
 *  <li>executiveStartTime: 巡检开始时间</li>
 *  <li>executiveExpireTime: 巡检结束时间</li>
 *  <li>executiveTime: 巡检执行上报时间</li>
 *  <li>processExpireTime: 维修结束时间</li>
 *  <li>processTime: 维修执行上报时间</li>
 *  <li>status: 执行状态 参考{@link com.everhomes.rest.equipment.EquipmentTaskStatus}</li>
 *  <li>result: 执行结果 参考{@link com.everhomes.rest.equipment.EquipmentTaskResult}</li>
 *  <li>reviewResult: 审阅结果 参考{@link com.everhomes.rest.equipment.ReviewResult}</li>
 *  <li>standardDescription: 标准内容</li>
 *  <li>pictureFlag: 是否需要拍照 0：否 1：是</li>
 * </ul>
 */
public class EquipmentTaskDTO {
	private Long id;
	
	private Long standardId;
	
	private String standardName;
	
	private Long templateId;
	
	private String templateName;
	
	private Long equipmentId;
	
	private String equipmentName;
	
	private String equipmentLocation;
	
	private Long parentId;
	
	private Long childCount;

	private String ownerType;
	
	private Long ownerId;

	private String targetType;

	private Long targetId;
	
	private Byte taskType;
	
	private String taskName;
	
	private String taskNumber;
	
	private String groupName;
	
	private Long executiveGroupId;
	
	private String executorType;
	
	private Long executorId;
	
	private String executorName;

	private String operatorType;
	
	private Long operatorId;
	
	private String operatorName;
	
	private Timestamp executiveStartTime;
	
	private Timestamp executiveTime;
	
	private Timestamp executiveExpireTime;
	
	private Timestamp processExpireTime;
	
	private Timestamp processTime;
	
	private Byte status;
	
	private Byte result;
	
	private Byte reviewResult;
	
	private Long reviewerId;
	
	private String reviewerType;
	
	private String reviewerName;

	private String standardDescription;
	
	private Byte qrCodeFlag;

	private Byte pictureFlag;

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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentLocation() {
		return equipmentLocation;
	}

	public void setEquipmentLocation(String equipmentLocation) {
		this.equipmentLocation = equipmentLocation;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getChildCount() {
		return childCount;
	}

	public void setChildCount(Long childCount) {
		this.childCount = childCount;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getExecutiveGroupId() {
		return executiveGroupId;
	}

	public void setExecutiveGroupId(Long executiveGroupId) {
		this.executiveGroupId = executiveGroupId;
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

	public Timestamp getProcessExpireTime() {
		return processExpireTime;
	}

	public void setProcessExpireTime(Timestamp processExpireTime) {
		this.processExpireTime = processExpireTime;
	}

	public Timestamp getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Timestamp processTime) {
		this.processTime = processTime;
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

	public String getStandardDescription() {
		return standardDescription;
	}

	public void setStandardDescription(String standardDescription) {
		this.standardDescription = standardDescription;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
