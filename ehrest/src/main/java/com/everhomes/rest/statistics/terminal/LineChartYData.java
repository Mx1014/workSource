package com.everhomes.rest.statistics.terminal;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 *<li>data:y轴数据<li>
 *</ul>
 */
public class LineChartYData {

	private String name;
	private List<Number> data;

    public LineChartYData() {
    }

    public LineChartYData(String name, List<Number> data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Number> getData() {
		return data;
	}

	public void setData(List<Number> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
