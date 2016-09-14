package com.everhomes.rest.equipment;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>processType: 操作类型 参考{@link com.everhomes.rest.equipment.EquipmentTaskProcessType}</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListLogsByTaskIdCommand {
	@NotNull
	private Long taskId;
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	@ItemType(Byte.class)
	private List<Byte> processType;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
