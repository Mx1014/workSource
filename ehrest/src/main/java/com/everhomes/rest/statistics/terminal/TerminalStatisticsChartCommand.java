package com.everhomes.rest.statistics.terminal;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *<ul>
 *<li>dates:日期字符串<li>
 *</ul>
 */
public class TerminalStatisticsChartCommand {

	@ItemType(String.class)
	private List<String> dates;

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}
}
