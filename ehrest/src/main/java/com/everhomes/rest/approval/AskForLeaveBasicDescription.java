// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>timeTotal: 请假总时长</li>
 * <li>categoryName: 请假类型</li>
 * <li>timeRangeList: 请假时间列表</li>
 * </ul>
 */
public class AskForLeaveBasicDescription {
	private String timeTotal;
	private String categoryName;
	@ItemType(TimeRange.class)
	private List<TimeRange> timeRangeList;

	public String getTimeTotal() {
		return timeTotal;
	}

	public void setTimeTotal(String timeTotal) {
		this.timeTotal = timeTotal;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<TimeRange> getTimeRangeList() {
		return timeRangeList;
	}

	public void setTimeRangeList(List<TimeRange> timeRangeList) {
		this.timeRangeList = timeRangeList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
