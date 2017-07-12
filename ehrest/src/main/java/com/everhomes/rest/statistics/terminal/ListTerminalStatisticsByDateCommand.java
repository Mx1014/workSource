package com.everhomes.rest.statistics.terminal;



/**
 *<ul>
 *<li>startDate:开始时间日期<li>
 *<li>endDate:结束时间日期<li>
 *</ul>
 */
public class ListTerminalStatisticsByDateCommand {

	private String startDate;

	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
