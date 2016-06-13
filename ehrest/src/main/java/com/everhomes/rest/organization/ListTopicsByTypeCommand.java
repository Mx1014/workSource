package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>pageOffset : 页码</li>
 *	<li>pageSize : 页大小</li>
 *	<li>organizationId : 组织id</li>
 *	<li>taskType : 任务类型 ，详情{@link com.everhomes.rest.organization.OrganizationTaskType}</li>
 *	<li>taskStatus : 任务状态，详情 {@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 *	<li>entrancePrivilege : 任务状态，详情 {@link com.everhomes.rest.ui.privilege.EntrancePrivilege}</li>
 *</ul>
 *
 */
public class ListTopicsByTypeCommand {
	
	private Long pageOffset;
	private Integer pageSize;
	@NotNull
	private Long organizationId;
	private String taskType;
	private Byte taskStatus;
	private Long communityId;
	
	private Long targetId;
	
	private String option;
	
	private String entrancePrivilege;
	
	public Byte getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(Byte taskStatus) {
		this.taskStatus = taskStatus;
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
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
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
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	

}
