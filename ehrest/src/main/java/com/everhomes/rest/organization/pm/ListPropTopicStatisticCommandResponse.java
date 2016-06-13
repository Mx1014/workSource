// @formatter:off
package com.everhomes.rest.organization.pm;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>todayList：当天数量列表</li>
 * <li>yesterdayList: 昨天数量列表</li>
 * <li>weekList: 上周数量列表</li>
 * <li>monthList: 上月数量列表</li>
 * <li>dateList: 时间点数量列表</li>
 * </ul>
 */
public class ListPropTopicStatisticCommandResponse {

	@ItemType(Integer.class)
	private List<Integer> todayList = new ArrayList<Integer>();
	@ItemType(Integer.class)
	private List<Integer> yesterdayList = new ArrayList<Integer>();
	@ItemType(Integer.class)
	private List<Integer> weekList = new ArrayList<Integer>();
	@ItemType(Integer.class)
	private List<Integer> monthList = new ArrayList<Integer>();
	@ItemType(Integer.class)
	private List<Integer> dateList = new ArrayList<Integer>();

	public ListPropTopicStatisticCommandResponse() {
	}

	public List<Integer> getTodayList() {
		return todayList;
	}

	public void setTodayList(List<Integer> todayList) {
		this.todayList = todayList;
	}

	public List<Integer> getYesterdayList() {
		return yesterdayList;
	}

	public void setYesterdayList(List<Integer> yesterdayList) {
		this.yesterdayList = yesterdayList;
	}

	public List<Integer> getWeekList() {
		return weekList;
	}

	public void setWeekList(List<Integer> weekList) {
		this.weekList = weekList;
	}

	public List<Integer> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<Integer> monthList) {
		this.monthList = monthList;
	}

	public List<Integer> getDateList() {
		return dateList;
	}

	public void setDateList(List<Integer> dateList) {
		this.dateList = dateList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
