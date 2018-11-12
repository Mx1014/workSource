package com.everhomes.rest.asset.calculate;

/**
 * @author created by ycx
 * @date 下午2:24:29
 */
/**
 *<ul>
 * <li>monthOffset: 自然季的月份偏移量</li>
 * <li>dateStrBegin: 自然季的开始日期</li>
 * <li>dateStrEnd: 自然季的结束日期</li>
 *</ul>
 */
public class NatualQuarterMonthDTO {
	private int monthOffset;
	private String dateStrBegin;
	private String dateStrEnd;
	
	public int getMonthOffset() {
		return monthOffset;
	}
	public void setMonthOffset(int monthOffset) {
		this.monthOffset = monthOffset;
	}
	public String getDateStrBegin() {
		return dateStrBegin;
	}
	public void setDateStrBegin(String dateStrBegin) {
		this.dateStrBegin = dateStrBegin;
	}
	public String getDateStrEnd() {
		return dateStrEnd;
	}
	public void setDateStrEnd(String dateStrEnd) {
		this.dateStrEnd = dateStrEnd;
	}

}
