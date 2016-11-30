package com.everhomes.rest.statistics.terminal;

/**
 *<ul>
 *<li>date:时间</li>
 *<li>newUserNumber:新增用户数</li>
 *<li>activeUserNumber:活跃用户数</li>
 *<li>startNumber:启动数</li>
 *<li>cumulativeUserNumber:累计用户数</li>
 *<li>startChangeRate:启动次数变化率</li>
 *<li>newChangeRate:新增用户变化率</li>
 *<li>activeChangeRate:活跃用户变化率</li>
 *<li>cumulativeChangeRate:累计用户变化率</li>
 *<li>sevenActiveUserNumber:7天活跃用户数</li>
 *<li>thirtyActiveUserNumber:30天活跃用户数</li>
 *<li>activeRate:活跃率</li>
 *</ul>
 */
public class TerminalDayStatisticsDTO {
	
	private String date;
	
	private Double newUserNumber;
	
	private Double activeUserNumber;

	private Double startNumber;

	private Double cumulativeUserNumber;

	private Double startChangeRate;

	private Double newChangeRate;

	private Double activeChangeRate;

	private Double cumulativeChangeRate;

	private Double sevenActiveUserNumber;

	private Double thirtyActiveUserNumber;

	private Double activeRate;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public Double getStartChangeRate() {
		return startChangeRate;
	}

	public void setStartChangeRate(Double startChangeRate) {
		this.startChangeRate = startChangeRate;
	}

	public Double getNewChangeRate() {
		return newChangeRate;
	}

	public void setNewChangeRate(Double newChangeRate) {
		this.newChangeRate = newChangeRate;
	}

	public Double getActiveChangeRate() {
		return activeChangeRate;
	}

	public void setActiveChangeRate(Double activeChangeRate) {
		this.activeChangeRate = activeChangeRate;
	}

	public Double getCumulativeChangeRate() {
		return cumulativeChangeRate;
	}

	public void setCumulativeChangeRate(Double cumulativeChangeRate) {
		this.cumulativeChangeRate = cumulativeChangeRate;
	}

	public Double getSevenActiveUserNumber() {
		return sevenActiveUserNumber;
	}

	public void setSevenActiveUserNumber(Double sevenActiveUserNumber) {
		this.sevenActiveUserNumber = sevenActiveUserNumber;
	}

	public Double getThirtyActiveUserNumber() {
		return thirtyActiveUserNumber;
	}

	public void setThirtyActiveUserNumber(Double thirtyActiveUserNumber) {
		this.thirtyActiveUserNumber = thirtyActiveUserNumber;
	}

	public Double getActiveRate() {
		return activeRate;
	}

	public void setActiveRate(Double activeRate) {
		this.activeRate = activeRate;
	}
}
