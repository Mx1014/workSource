package com.everhomes.rest.statistics.transaction;

/**
 *<ul>
 *<li>taskType:任務類型<li>
 *</ul>
 */
public class ExecuteTaskCommand {

	private Long startDate;
	
	private Long endDate;

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	
}
