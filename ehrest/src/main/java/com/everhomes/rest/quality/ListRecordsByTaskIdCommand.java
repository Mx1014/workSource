package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

public class ListRecordsByTaskIdCommand {
	
	private Long taskId;
	
	
	public Long getTaskId() {
		return taskId;
	}


	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
