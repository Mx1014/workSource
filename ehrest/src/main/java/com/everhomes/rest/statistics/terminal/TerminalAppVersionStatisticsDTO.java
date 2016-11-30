package com.everhomes.rest.statistics.terminal;

/**
 *<ul>
 *<li>date:时间</li>
 *<li>appVersion:版本</li>
 *<li>newUserNumber:新增用户数</li>
 *<li>activeUserNumber:活跃用户数</li>
 *<li>startNumber:启动数</li>
 *<li>cumulativeUserNumber:累计用户数</li>
 *<li>versionCumulativeRate:版本累计用户占比</li>
 *<li>versionActiveRate:版本活跃用户占比</li>
 *</ul>
 */
public class TerminalAppVersionStatisticsDTO {
	
	private String date;

	private String appVersion;
	
	private Double newUserNumber;
	
	private Double activeUserNumber;

	private Double startNumber;

	private Double cumulativeUserNumber;

	private Double versionCumulativeRate;

	private Double versionActiveRate;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
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

	public Double getVersionCumulativeRate() {
		return versionCumulativeRate;
	}

	public void setVersionCumulativeRate(Double versionCumulativeRate) {
		this.versionCumulativeRate = versionCumulativeRate;
	}

	public Double getVersionActiveRate() {
		return versionActiveRate;
	}

	public void setVersionActiveRate(Double versionActiveRate) {
		this.versionActiveRate = versionActiveRate;
	}
}
