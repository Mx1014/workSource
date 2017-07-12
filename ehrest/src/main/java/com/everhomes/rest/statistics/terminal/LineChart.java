package com.everhomes.rest.statistics.terminal;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *<ul>
 * <li>xData:x轴数据<li>
 * <li>yData:y轴数据<li>
 *</ul>
 */
public class LineChart {

	private ChartXData xData;

	@ItemType(LineChartYData.class)
	private List<LineChartYData> yData;

	public ChartXData getxData() {
		return xData;
	}

	public void setxData(ChartXData xData) {
		this.xData = xData;
	}

	public List<LineChartYData> getyData() {
		return yData;
	}

	public void setyData(List<LineChartYData> yData) {
		this.yData = yData;
	}
}
