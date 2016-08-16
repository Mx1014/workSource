package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

public class CreateTaskCategoryCommand {
	private Integer namspaceId;
	private String name;
	
	public Integer getNamspaceId() {
		return namspaceId;
	}
	public void setNamspaceId(Integer namspaceId) {
		this.namspaceId = namspaceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
