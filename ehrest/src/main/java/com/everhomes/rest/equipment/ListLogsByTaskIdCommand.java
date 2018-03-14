package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>taskId: 任务id (批量审批改成id列表)</li>
 *  <li>equipmentId: 设备id (有id则是查看具体设备下的log)</li>
 *  <li>standardId: 标准id (有id则是查看具体设备下的log)</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>processType: 操作类型 参考{@link com.everhomes.rest.equipment.EquipmentTaskProcessType}</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListLogsByTaskIdCommand {
	@ItemType(Long.class)
	private List<Long> taskId;
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	@ItemType(Byte.class)
	private List<Byte> processType;

	private Long equipmentId;

	private Long standardId;

	private Long pageAnchor;
	
	private Integer pageSize;

	public List<Long> getTaskId() {
		return taskId;
	}

	public void setTaskId(List<Long> taskId) {
		this.taskId = taskId;
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

	public List<Byte> getProcessType() {
		return processType;
	}

	public void setProcessType(List<Byte> processType) {
		this.processType = processType;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
