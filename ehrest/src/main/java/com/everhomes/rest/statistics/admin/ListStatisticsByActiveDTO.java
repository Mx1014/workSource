package com.everhomes.rest.statistics.admin;

import java.sql.Timestamp;

public class ListStatisticsByActiveDTO {
	
	private Integer yesterdayActiveCount;
	
	private Integer lastWeekActiveCount;
	
	private Integer lastMonthActiveCount;
	
	private double ystToLastWeekRatio;
	
	private double ystToLastMonthRatio;
	
	private Timestamp createTime;
	
	private Integer activeCount;
	
	private double dayActiveToSearchRatio;

	public Integer getYesterdayActiveCount() {
		return yesterdayActiveCount;
	}

	public void setYesterdayActiveCount(Integer yesterdayActiveCount) {
		this.yesterdayActiveCount = yesterdayActiveCount;
	}

	public Integer getLastWeekActiveCount() {
		return lastWeekActiveCount;
	}

	public void setLastWeekActiveCount(Integer lastWeekActiveCount) {
		this.lastWeekActiveCount = lastWeekActiveCount;
	}

	public Integer getLastMonthActiveCount() {
		return lastMonthActiveCount;
	}

	public void setLastMonthActiveCount(Integer lastMonthActiveCount) {
		this.lastMonthActiveCount = lastMonthActiveCount;
	}

	public double getYstToLastWeekRatio() {
		return ystToLastWeekRatio;
	}

	public void setYstToLastWeekRatio(double ystToLastWeekRatio) {
		this.ystToLastWeekRatio = ystToLastWeekRatio;
	}

	public double getYstToLastMonthRatio() {
		return ystToLastMonthRatio;
	}

	public void setYstToLastMonthRatio(double ystToLastMonthRatio) {
		this.ystToLastMonthRatio = ystToLastMonthRatio;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}

	public double getDayActiveToSearchRatio() {
		return dayActiveToSearchRatio;
	}

	public void setDayActiveToSearchRatio(double dayActiveToSearchRatio) {
		this.dayActiveToSearchRatio = dayActiveToSearchRatio;
	}

}
