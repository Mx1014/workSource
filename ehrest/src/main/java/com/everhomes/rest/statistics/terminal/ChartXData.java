package com.everhomes.rest.statistics.terminal;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *<ul>
 *<li>data:x轴数据<li>
 *</ul>
 */
public class ChartXData {

	@ItemType(String.class)
	private List<String> data;

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}
}
