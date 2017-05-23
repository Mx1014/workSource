package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>categoryId: 分类ID</li>
 * <li>priority: 客户反映</li>
 * <li>reserveTime: 预约时间</li>
 * <li>sourceType: 报事来源</li>
 * <li>taskId: 任务ID</li>
 * </ul>
 */
public class UpdateTaskCommand {
	private Integer namespaceId;
	private String ownerType;
    private Long ownerId;
	private Long taskId;
	private Long categoryId;
	private Byte priority;
	private String sourceType;
	private Long reserveTime;

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
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Byte getPriority() {
		return priority;
	}
	public void setPriority(Byte priority) {
		this.priority = priority;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getReserveTime() {
		return reserveTime;
	}
	public void setReserveTime(Long reserveTime) {
		this.reserveTime = reserveTime;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
