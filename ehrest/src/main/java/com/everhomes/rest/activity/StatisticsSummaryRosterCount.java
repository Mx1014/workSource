// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;
import java.util.Map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>dayCount: 当天数量</li>
 * <li>weekCount: 当周数量</li>
 * <li>monthCount: 当月数量</li>
 * <li>manCount: 男性数量</li>
 * <li>womanCount: 女性数量</li>
 * <li>everydayMap: 每天的数量Map</li>
 * </ul>
 */

public class StatisticsSummaryRosterCount{
	private Integer dayCount;
	private Integer weekCount;
	private Integer monthCount;
	private Integer manCount;
	private Integer womanCount;
	private Map<String, Integer> everydayMap;
	
	public Integer getDayCount() {
		return dayCount;
	}
	public void setDayCount(Integer dayCount) {
		this.dayCount = dayCount;
	}
	public Integer getWeekCount() {
		return weekCount;
	}
	public void setWeekCount(Integer weekCount) {
		this.weekCount = weekCount;
	}
	public Integer getMonthCount() {
		return monthCount;
	}
	public void setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
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
	public Map<String, Integer> getEverydayMap() {
		return everydayMap;
	}
	public void setEverydayMap(Map<String, Integer> everydayMap) {
		this.everydayMap = everydayMap;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}