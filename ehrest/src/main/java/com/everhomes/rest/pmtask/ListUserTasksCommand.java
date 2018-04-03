package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>status: 0: 未处理  1: 已处理 {@link com.everhomes.rest.pmtask.PmTaskProcessStatus}</li>
 * <li>pageAnchor: 分页瞄</li>
 * <li>pageSize: 每页条数</li>
 * <li>organizationId: 机构id</li>
 * <li>taskCategoryId: 服务类型id</li>
 * </ul>
 */
public class ListUserTasksCommand {
	private Integer namespaceId;
	private String ownerType;
    private Long ownerId;
	private Byte status;
    private Long pageAnchor;
	private Integer pageSize;
	private Long organizationId;
	private Long taskCategoryId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getTaskCategoryId() {
		return taskCategoryId;
	}
	public void setTaskCategoryId(Long taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}
}
