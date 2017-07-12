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
	
	private Long newUserNumber;
	
	private Long activeUserNumber;

	private Long startNumber;

	private Long cumulativeUserNumber;

	private Double startChangeRate;

	private Double newChangeRate;

	private Double activeChangeRate;

	private Double cumulativeChangeRate;

	private Long sevenActiveUserNumber;

	private Long thirtyActiveUserNumber;

	private Double activeRate;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getNewUserNumber() {
		return newUserNumber;
	}

	public void setNewUserNumber(Long newUserNumber) {
		this.newUserNumber = newUserNumber;
	}

	public Long getActiveUserNumber() {
		return activeUserNumber;
	}

	public void setActiveUserNumber(Long activeUserNumber) {
		this.activeUserNumber = activeUserNumber;
	}

	public Long getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(Long startNumber) {
		this.startNumber = startNumber;
	}

	public Long getCumulativeUserNumber() {
		return cumulativeUserNumber;
	}

	public void setCumulativeUserNumber(Long cumulativeUserNumber) {
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

	public Long getSevenActiveUserNumber() {
		return sevenActiveUserNumber;
	}

	public void setSevenActiveUserNumber(Long sevenActiveUserNumber) {
		this.sevenActiveUserNumber = sevenActiveUserNumber;
	}

	public Long getThirtyActiveUserNumber() {
		return thirtyActiveUserNumber;
	}

	public void setThirtyActiveUserNumber(Long thirtyActiveUserNumber) {
		this.thirtyActiveUserNumber = thirtyActiveUserNumber;
	}

	public Double getActiveRate() {
		return activeRate;
	}

	public void setActiveRate(Double activeRate) {
		this.activeRate = activeRate;
	}
}
