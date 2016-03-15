package com.everhomes.rest.organization;

/**
 * <ul>
 * 	<li>pageOffset : 页码</li>
 *	<li>pageSize : 页大小</li>
 *  <li>taskType : 任务类型 ，详情{@link com.everhomes.rest.organization.OrganizationTaskType}</li>
 *  <li>taskStatus : 任务状态 ，详情{@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 * </ul>
 *
 */
public class ListUserTaskCommand {

	private Long pageOffset;
	
	private Integer pageSize;
	
	private String taskType;
	
	private Long communityId;
	
	private Long organizationId;
	
	private Long taskStatus;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Long getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Long pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Long taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	
	
}
