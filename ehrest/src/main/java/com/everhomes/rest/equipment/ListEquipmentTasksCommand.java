package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>ownerId: 任务所属组织等的id</li>
 *  <li>ownerType: 任务所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 任务所属管理处id</li>
 *  <li>targetType: 任务所属管理处类型</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>isReview: 是否查审阅任务 0：执行任务；1：审核任务</li>
 *  <li>taskStatus: 任务状态列表 参考 {@link com.everhomes.rest.equipment.EquipmentTaskStatus}</li>
 * </ul>
 */
public class ListEquipmentTasksCommand {
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;
	
	private String targetType;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private Long inspectionCategoryId;
	
	private Byte isReview;
	@ItemType(Byte.class)
	private List<Byte> taskStatus;

	public Byte getIsReview() {
		return isReview;
	}

	public void setIsReview(Byte isReview) {
		this.isReview = isReview;
	}

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

	public List<Byte> getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(List<Byte> taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
