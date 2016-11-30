package com.everhomes.rest.statistics.terminal;

/**
 *<ul>
 *<li>date:时间</li>
 *<li>newUserNumber:新增用户数</li>
 *<li>activeUserNumber:活跃用户数</li>
 *<li>startNumber:启动数</li>
 *<li>cumulativeUserNumber:累计用户数</li>
 *<li>hour:小时</li>
 *</ul>
 */
public class TerminalHourStatisticsDTO {
	
	private String date;

	private String hour;
	
	private Double newUserNumber;
	
	private Double activeUserNumber;

	private Double startNumber;

	private Double cumulativeUserNumber;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public Double getNewUserNumber() {
		return newUserNumber;
	}

	public void setNewUserNumber(Double newUserNumber) {
		this.newUserNumber = newUserNumber;
	}

	public Double getActiveUserNumber() {
		return activeUserNumber;
	}

	public void setActiveUserNumber(Double activeUserNumber) {
		this.activeUserNumber = activeUserNumber;
	}

	public Double getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(Double startNumber) {
		this.startNumber = startNumber;
	}

	public Double getCumulativeUserNumber() {
		return cumulativeUserNumber;
	}

	public void setCumulativeUserNumber(Double cumulativeUserNumber) {
		this.cumulativeUserNumber = cumulativeUserNumber;
	}
}
