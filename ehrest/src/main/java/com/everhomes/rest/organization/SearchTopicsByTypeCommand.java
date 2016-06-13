package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>communityId: 用户当前所有小区ID</li>
 *  <li>organizationId : 组织id</li>
 *  <li>keyword: 帖子标题、内容关键字</li>
 *  <li>taskStatus : 任务状态，详情 {@link com.everhomes.rest.organization.OrganizationTaskStatus}</li>
 *  <li>taskType : 任务类型 ，详情{@link com.everhomes.rest.organization.OrganizationTaskType}</li>
 *  <li>flag: 标识是我的任务查询还是所有任务查询</li>
 *  <li>pageAnchor: 本页开始锚点</li>
 *  <li>pageSize: 每页的数量</li>
 * </ul>
 *
 */
public class SearchTopicsByTypeCommand {

	private Long communityId;
	
	@NotNull
	private Long organizationId;
    
	private String keyword;
	
	private Byte taskStatus;
	
	private String taskType;
	
	private String flag;

    private Long pageAnchor;
    
    private Integer pageSize;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Byte getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Byte taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
