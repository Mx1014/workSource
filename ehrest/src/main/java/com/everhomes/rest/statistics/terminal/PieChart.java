package com.everhomes.rest.statistics.terminal;


import java.util.List;

/**
 *<ul>
 *<li>data:饼图数据<li>
 *</ul>
 */
public class PieChart {

	private List<PieChartData> data;

	public List<PieChartData> getData() {
		return data;
	}

	public void setData(List<PieChartData> data) {
		this.data = data;
	}
}
