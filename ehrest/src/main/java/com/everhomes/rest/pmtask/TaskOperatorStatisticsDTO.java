package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

public class TaskOperatorStatisticsDTO {

	private String operatorName;
	private Long avgStar;
	private Long taskCategoryId;
	private String taskCategoryName;
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Long getAvgStar() {
		return avgStar;
	}
	public void setAvgStar(Long avgStar) {
		this.avgStar = avgStar;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
