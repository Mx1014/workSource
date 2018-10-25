package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>ownerId: 任务所属组织等的id</li>
 *  <li>ownerType: 任务所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 任务所属管理处id</li>
 *  <li>targetType: 任务所属管理处类型</li>
 *  <li>startTime: 开始时间</li>
 *  <li>endTime: 截止时间</li>
 *  <li>status: 任务状态 list参考{@link com.everhomes.rest.equipment.EquipmentTaskStatus}</li>
 *  <li>reviewStatus: 任务审核状态 0: UNREVIEWED  1: REVIEWED</li>
 *  <li>taskType: 类型 参考{@link com.everhomes.rest.equipment.StandardType}</li>
 *  <li>keyword: 查询关键字</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class SearchEquipmentTasksCommand {
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;
	@ItemType(Long.class)
	private List<Long> targetIds;

	private String targetIdString;
	
	private String targetType;
	
	private Long startTime;
	
	private Long endTime;

	@ItemType(Byte.class)
	private List<Byte> status;
	
	private Byte reviewStatus;
	
	private Byte taskType;
	
	private String keyword;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private Long inspectionCategoryId;

	private Integer namespaceId;
	
	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
	}
	
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

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public List<Byte> getStatus() {
		return status;
	}

	public void setStatus(List<Byte> status) {
		this.status = status;
	}

	public Byte getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Byte reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Byte getTaskType() {
		return taskType;
	}

	public void setTaskType(Byte taskType) {
		this.taskType = taskType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public List<Long> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<Long> targetIds) {
		this.targetIds = targetIds;
	}

	public String getTargetIdString() {
		return targetIdString;
	}

	public void setTargetIdString(String targetIdString) {
		this.targetIdString = targetIdString;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
