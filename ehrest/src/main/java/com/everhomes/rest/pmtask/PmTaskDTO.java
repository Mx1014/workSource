package com.everhomes.rest.pmtask;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 任务log Id</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>categoryId: 子类型id</li>
 * <li>address: 服务地点</li>
 * <li>content: 内容</li>
 * <li>status: 状态 1: 未处理  2: 处理中 3: 已完成  4: 已关闭{@link com.everhomes.rest.pmtask.PmTaskStatus}</li>
 * <li>star: 评价分数</li>
 * <li>unprocessedTime: 未处理</li>
 * <li>processingTime: 处理中时间</li>
 * <li>processedTime: 完成时间</li>
 * <li>closedTime: 关闭时间</li>
 * <li>creatorUid: 创建人id</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class PmTaskDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long categoryId;
	private String address;
	private String content;
	private Byte status;
	private Byte star;
	private Timestamp unprocessedTime;
	private Timestamp processingTime;
	private Timestamp processedTime;
	private Timestamp closedTime;
	private Long creatorUid;
	private Timestamp createTime;
	
	@ItemType(PmTaskLogDTO.class)
	private List<PmTaskLogDTO> taskLogs;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public Byte getStar() {
		return star;
	}
	public void setStar(Byte star) {
		this.star = star;
	}
	public Timestamp getUnprocessedTime() {
		return unprocessedTime;
	}
	public void setUnprocessedTime(Timestamp unprocessedTime) {
		this.unprocessedTime = unprocessedTime;
	}
	public Timestamp getProcessingTime() {
		return processingTime;
	}
	public void setProcessingTime(Timestamp processingTime) {
		this.processingTime = processingTime;
	}
	public Timestamp getProcessedTime() {
		return processedTime;
	}
	public void setProcessedTime(Timestamp processedTime) {
		this.processedTime = processedTime;
	}
	public Timestamp getClosedTime() {
		return closedTime;
	}
	public void setClosedTime(Timestamp closedTime) {
		this.closedTime = closedTime;
	}
	public Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public List<PmTaskLogDTO> getTaskLogs() {
		return taskLogs;
	}
	public void setTaskLogs(List<PmTaskLogDTO> taskLogs) {
		this.taskLogs = taskLogs;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
