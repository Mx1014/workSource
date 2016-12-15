package com.everhomes.rest.statistics.terminal;


import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *<ul>
 *<li>data:y轴数据<li>
 *</ul>
 */
public class LineChartYData {

	private String name;

	@ItemType(Double.class)
	private List<Double> data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Double> getData() {
		return data;
	}

	public void setData(List<Double> data) {
		this.data = data;
	}
}
