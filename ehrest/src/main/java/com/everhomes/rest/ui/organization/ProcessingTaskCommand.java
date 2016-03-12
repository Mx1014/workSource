package com.everhomes.rest.ui.organization;

import javax.validation.constraints.NotNull;


/**
 * <ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>taskId: 任务ID</li>
 * <li>userId: 用户ID </li>
 * <li>taskStatus: 处理状态，参考{@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 * <li>taskCategory: 类别，参考{@link com.everhomes.rest.organization.OrganizationTaskCategory}</li>
 * <li>privateFlag: 可见性，参考{@link com.everhomes.rest.organization.PrivateFlag}</li>
 * </ul>
 */
public class ProcessingTaskCommand {
	
	private String sceneToken;
	
	@NotNull
	private Long taskId;
	
	private Long userId;
	
	private Byte taskStatus;
	
	private String taskCategory;
	
	private Byte privateFlag;
	
	

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
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
