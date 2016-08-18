package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>ownerId: 任务所属组织等的id</li>
 *  <li>ownerType: 任务所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * </ul>
 */
public class ListTaskByIdCommand {
	@NotNull
	private Long taskId;
	@NotNull
	private Long ownerId;
	@NotNull
	private String ownerType;
	
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
