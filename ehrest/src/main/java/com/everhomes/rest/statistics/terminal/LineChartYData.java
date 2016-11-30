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

	@ItemType(String.class)
	private List<String> data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}
}
