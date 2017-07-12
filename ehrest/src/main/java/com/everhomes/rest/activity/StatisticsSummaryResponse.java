// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>activityCount: 活动总数</li>
 * <li>activityDayCount: 当天活动总数</li>
 * <li>activityWeekCount: 当周活动总数</li>
 * <li>activityMonthCount: 当月活动总数</li>
 * <li>rosterCount: 报名总数</li>
 * <li>rosterDayCount: 当天报名总数</li>
 * <li>rosterWeekCount: 当周报名总数</li>
 * <li>rosterMonthCount: 当月报名总数</li>
 * <li>manCount: 男性总数</li>
 * <li>womanCount: 女性总数</li>
 * </ul>
 */
public class StatisticsSummaryResponse {
	
	private Integer activityCount;
	
	private Integer activityDayCount;
	
	private Integer activityWeekCount;
	
	private Integer activityMonthCount;
	
	private Integer rosterCount;
	
	private Integer rosterDayCount;
	
	private Integer rosterWeekCount;
	
	private Integer rosterMonthCount;
	
	private Integer manCount;
	
	private Integer womanCount;
	
	public Integer getActivityCount() {
		return activityCount;
	}

	public void setActivityCount(Integer activityCount) {
		this.activityCount = activityCount;
	}

	public Integer getActivityDayCount() {
		return activityDayCount;
	}

	public void setActivityDayCount(Integer activityDayCount) {
		this.activityDayCount = activityDayCount;
	}

	public Integer getActivityWeekCount() {
		return activityWeekCount;
	}

	public void setActivityWeekCount(Integer activityWeekCount) {
		this.activityWeekCount = activityWeekCount;
	}

	public Integer getActivityMonthCount() {
		return activityMonthCount;
	}

	public void setActivityMonthCount(Integer activityMonthCount) {
		this.activityMonthCount = activityMonthCount;
	}

	public Integer getRosterCount() {
		return rosterCount;
	}

	public void setRosterCount(Integer rosterCount) {
		this.rosterCount = rosterCount;
	}
	public Integer getRosterDayCount() {
		return rosterDayCount;
	}

	public void setRosterDayCount(Integer rosterDayCount) {
		this.rosterDayCount = rosterDayCount;
	}

	public Integer getRosterWeekCount() {
		return rosterWeekCount;
	}

	public void setRosterWeekCount(Integer rosterWeekCount) {
		this.rosterWeekCount = rosterWeekCount;
	}

	public Integer getRosterMonthCount() {
		return rosterMonthCount;
	}

	public void setRosterMonthCount(Integer rosterMonthCount) {
		this.rosterMonthCount = rosterMonthCount;
	}

	public Integer getManCount() {
		return manCount;
	}

	public void setManCount(Integer manCount) {
		this.manCount = manCount;
	}

	public Integer getWomanCount() {
		return womanCount;
	}

	public void setWomanCount(Integer womanCount) {
		this.womanCount = womanCount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
