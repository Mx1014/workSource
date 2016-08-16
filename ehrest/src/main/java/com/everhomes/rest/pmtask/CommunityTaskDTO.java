package com.everhomes.rest.pmtask;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class CommunityTaskDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long serviceCategoryId;
	private String serviceCategoryName;
	private Long categoryId;
	private String categoryName;
	private String address;
	private String content;
	private Byte status;
	private Long evaluateScore;
	private Timestamp unprocessedTime;
	private Timestamp processingTime;
	private Timestamp processedTime;
	private Timestamp closedTime;
	private Long creatorUid;
	private Timestamp createTime;
	@ItemType(CommunityTaskLogDTO.class)
	private List<CommunityTaskLogDTO> taskLogs;
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
	public Long getServiceCategoryId() {
		return serviceCategoryId;
	}
	public void setServiceCategoryId(Long serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}
	public String getServiceCategoryName() {
		return serviceCategoryName;
	}
	public void setServiceCategoryName(String serviceCategoryName) {
		this.serviceCategoryName = serviceCategoryName;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	public Long getEvaluateScore() {
		return evaluateScore;
	}
	public void setEvaluateScore(Long evaluateScore) {
		this.evaluateScore = evaluateScore;
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
	public List<CommunityTaskLogDTO> getTaskLogs() {
		return taskLogs;
	}
	public void setTaskLogs(List<CommunityTaskLogDTO> taskLogs) {
		this.taskLogs = taskLogs;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
