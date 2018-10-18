package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *  <li>ownerId: 任务所属组织等的id</li>
 *  <li>ownerType: 任务所属组织类型，如enterprise</li>
 *  <li>targetId: 任务所属项目id</li>
 *  <li>targetType: 任务所属项目</li>
 *  <li>taskType: 任务类型</li>
 *  <li>executeFlag: 是否按任务执行人查询任务 0-否 1-是</li>
 *  <li>isReview: 是否是审阅 0-否 1-是</li>
 *  <li>startDate: 任务开始日期</li>
 *  <li>endDate: 任务结束日期</li>
 *  <li>groupId: 执行任务组id</li>
 *  <li>executeStatus: 执行状态</li>
 *  <li>reviewStatus: 审阅状态</li>
 *  <li>taskName: 任务名称</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>manualFlag: 是否手动添加 0：自动创建， 2: 绩效考核</li>
 *  <li>namespaceId: namespaceId </li>
 *  <li>lastSyncTime: 用于离线 上次同步时间 </li>
 * </ul>
 */
public class ListQualityInspectionTasksCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Long targetId;

	@ItemType(Long.class)
	private List<Long> targetIds;
	
	private String targetType;
	
	@NotNull
	private Byte taskType;
	
	private Byte executeFlag;
	
	private Byte isReview;
	
	private Long startDate;
	
	private Long endDate;
	
	private Long groupId;

	@ItemType(Byte.class)
	private List<Byte> executeStatus;

	private Byte reviewStatus;

	private String taskName;
	
	private Byte manualFlag;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	private Integer namespaceId;

	private String lastSyncTime;

	private Timestamp latestUpdateTime;
	
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Byte getTaskType() {
		return taskType;
	}

	public void setTaskType(Byte taskType) {
		this.taskType = taskType;
	}

	public Byte getExecuteFlag() {
		return executeFlag;
	}

	public void setExecuteFlag(Byte executeFlag) {
		this.executeFlag = executeFlag;
	}

	public Byte getIsReview() {
		return isReview;
	}

	public void setIsReview(Byte isReview) {
		this.isReview = isReview;
	}

	public List<Byte> getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(List<Byte> executeStatus) {
		this.executeStatus = executeStatus;
	}

	public Byte getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Byte reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Byte getManualFlag() {
		return manualFlag;
	}

	public void setManualFlag(Byte manualFlag) {
		this.manualFlag = manualFlag;
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(String lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	public Timestamp getLatestUpdateTime() {
		return latestUpdateTime;
	}

	public void setLatestUpdateTime(Timestamp latestUpdateTime) {
		this.latestUpdateTime = latestUpdateTime;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public List<Long> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<Long> targetIds) {
		this.targetIds = targetIds;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
