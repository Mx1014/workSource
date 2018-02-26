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
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>taskStatus: 任务状态列表 参考 {@link com.everhomes.rest.equipment.EquipmentTaskStatus}</li>
 *  <li>lastSyncTime: 上次同步MAX_TIME offline</li>
 *  <li>moduleName: equipment_inspection offline </li>
 *  <li>communityId: 动态表单项目id offline</li>
 *  <li>namespaceId: namespaceId</li>
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

	private String lastSyncTime;

	private String moduleName;

	private Integer namespaceId;

	private Long communityId;

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

	public String getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(String lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}


	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
