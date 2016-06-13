// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 当前小区ID</li>
 * <li>taskType: 任务类型{@link com.everhomes.OrganizationTaskType.PmTaskType}</li>
 * <li>taskStatus: 任务状态{@link com.everhomes.rest.organization.OrganizationTaskStatus.PmTaskStatus}</li>
 * <li>pageOffset: 当前页码</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class QueryPropTopicByCategoryCommand {
    private Long communityId;
    private Long actionCategory;
    private Byte taskStatus;
    private Integer pageOffset;
    private Integer pageSize;
    
    public QueryPropTopicByCategoryCommand() {
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Byte getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Byte taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Long getActionCategory() {
		return actionCategory;
	}

	public void setActionCategory(Long actionCategory) {
		this.actionCategory = actionCategory;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
