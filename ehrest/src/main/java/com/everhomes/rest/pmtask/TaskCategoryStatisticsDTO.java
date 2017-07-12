package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class TaskCategoryStatisticsDTO {
	private Long ownerId;
	private String ownerName;
	private Long taskCategoryId;
	private String taskCategoryName;
	
	@ItemType(CategoryStatisticsDTO.class)
	private List<CategoryStatisticsDTO> requests;
	
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getTaskCategoryId() {
		return taskCategoryId;
	}
	public void setTaskCategoryId(Long taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}
	public String getTaskCategoryName() {
		return taskCategoryName;
	}
	public void setTaskCategoryName(String taskCategoryName) {
		this.taskCategoryName = taskCategoryName;
	}
	
	public List<CategoryStatisticsDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<CategoryStatisticsDTO> requests) {
		this.requests = requests;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
