package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;


/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>taskId: 任务ID</li>
 * <li>userId: 用户ID </li>
 * <li>taskStatus: 处理状态，参考{@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 * <li>taskCategory: 类别，参考{@link com.everhomes.rest.organization.OrganizationTaskCategory}</li>
 * <li>privateFlag: 可见性，参考{@link com.everhomes.rest.organization.PrivateFlag}</li>
 * </ul>
 */
public class ProcessOrganizationTaskCommand {
	
	private Long organizationId;
	
	@NotNull
	private Long taskId;
	
	private Long userId;
	
	private Byte taskStatus;
	
	private String taskCategory;
	
	private Byte privateFlag;
	
	

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Byte taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}

	public Byte getPrivateFlag() {
		return privateFlag;
	}

	public void setPrivateFlag(Byte privateFlag) {
		this.privateFlag = privateFlag;
	}

	
}
