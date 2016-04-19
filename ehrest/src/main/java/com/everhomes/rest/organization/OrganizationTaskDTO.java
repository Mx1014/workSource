// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: 任务ID</li>
 * <li>organizationId: 机构id </li>
 * <li>organizationType : 机构类型，参考{@link com.everhomes.rest.organization.OrganizationType}
 * <li>applyEntityType: 承载任务的实体类型，参考{@link com.everhomes.rest.organization.OrganizationTaskApplyEnityType}</li>
 * <li>applyEntityId: 承载任务的实体类型对应的ID，如帖子id</li>
 * <li>targetType: 任务被指派的目标类型，参考{@link com.everhomes.rest.organization.OrganizationTaskTargetType}</li>
 * <li>targetId: 任务被指派的目标类型对应的ID，如用户ID</li>
 * <li>taskType: 任务所属类型，参考{@link com.everhomes.rest.organization.OrganizationTaskType}</li>
 * <li>description: 任务描述</li>
 * <li>taskStatus: 任务状态，参考{@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 * <li>operatorUid : 任务操作user</li>
 * <li>operateTime : 操作时间</li>
 * <li>creatorUid : 创建用户</li>
 * <li>createTime : 创建时间</li>
 * <li>unprocessedTime : 任务未处理时间</li>
 * <li>processingTime : 任务处理中时间</li>
 * <li>processedTime : 任务已处理时间</li>
 * </ul>
 */
public class OrganizationTaskDTO {
	//task
	private java.lang.Long     id;
	private java.lang.Long     organizationId;
	private java.lang.String   organizationType;
	private java.lang.String   applyEntityType;
	private java.lang.Long     applyEntityId;
	private java.lang.String   targetType;
	private java.lang.Long     targetId;
	private java.lang.String   taskType;
	private java.lang.String   description;
	private java.lang.Byte     taskStatus;
	private java.lang.Long     operatorUid;
	private java.sql.Timestamp operateTime;
	private java.lang.Long     creatorUid;
	private java.sql.Timestamp createTime;
	private java.sql.Timestamp unprocessedTime;
	private java.sql.Timestamp processingTime;
	private java.sql.Timestamp processedTime;
	private String taskCategory;
	private String option;
	private String entrancePrivilege;
	private String targetName;
	
	private String targetToken;
	
    public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(java.lang.Long organizationId) {
		this.organizationId = organizationId;
	}

	public java.lang.String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(java.lang.String organizationType) {
		this.organizationType = organizationType;
	}

	public java.lang.String getApplyEntityType() {
		return applyEntityType;
	}

	public void setApplyEntityType(java.lang.String applyEntityType) {
		this.applyEntityType = applyEntityType;
	}

	public java.lang.Long getApplyEntityId() {
		return applyEntityId;
	}

	public void setApplyEntityId(java.lang.Long applyEntityId) {
		this.applyEntityId = applyEntityId;
	}

	public java.lang.String getTargetType() {
		return targetType;
	}

	public void setTargetType(java.lang.String targetType) {
		this.targetType = targetType;
	}

	public java.lang.Long getTargetId() {
		return targetId;
	}

	public void setTargetId(java.lang.Long targetId) {
		this.targetId = targetId;
	}

	public java.lang.String getTaskType() {
		return taskType;
	}

	public void setTaskType(java.lang.String taskType) {
		this.taskType = taskType;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.Byte getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(java.lang.Byte taskStatus) {
		this.taskStatus = taskStatus;
	}

	public java.lang.Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(java.lang.Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public java.sql.Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(java.sql.Timestamp operateTime) {
		this.operateTime = operateTime;
	}

	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	
	public java.sql.Timestamp getUnprocessedTime() {
		return unprocessedTime;
	}

	public void setUnprocessedTime(java.sql.Timestamp unprocessedTime) {
		this.unprocessedTime = unprocessedTime;
	}

	public java.sql.Timestamp getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(java.sql.Timestamp processingTime) {
		this.processingTime = processingTime;
	}

	public java.sql.Timestamp getProcessedTime() {
		return processedTime;
	}

	public void setProcessedTime(java.sql.Timestamp processedTime) {
		this.processedTime = processedTime;
	}

	
	public String getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	
	public String getEntrancePrivilege() {
		return entrancePrivilege;
	}

	public void setEntrancePrivilege(String entrancePrivilege) {
		this.entrancePrivilege = entrancePrivilege;
	}

	
	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetToken() {
		return targetToken;
	}

	public void setTargetToken(String targetToken) {
		this.targetToken = targetToken;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
