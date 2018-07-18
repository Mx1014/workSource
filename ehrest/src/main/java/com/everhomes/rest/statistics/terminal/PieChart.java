package com.everhomes.rest.statistics.terminal;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 *<li>data:饼图数据</li>
 *</ul>
 */
public class PieChart {

	@ItemType(PieChartData.class)
	private List<PieChartData> data;

	public PieChart() {
	}

	public PieChart(List<PieChartData> data) {
		this.data = data;
	}

	public List<PieChartData> getData() {
		return data;
	}

	public void setData(List<PieChartData> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
