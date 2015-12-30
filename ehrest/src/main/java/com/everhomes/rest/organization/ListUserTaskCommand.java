package com.everhomes.rest.organization;

/**
 * <ul>
 * 	<li>pageOffset : 页码</li>
 *	<li>pageSize : 页大小</li>
 *  <li>taskType : 任务类型 ，详情{@link com.everhomes.rest.organization.OrganizationTaskType}</li>
 * </ul>
 *
 */
public class ListUserTaskCommand {

	private Long pageOffset;
	
	private Integer pageSize;
	
	private String taskType;

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
	
}
