package com.everhomes.rest.repeat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>year: 年</li>
 *  <li>month: 月</li>
 *  <li>day: 日</li>
 * </ul>
 */
public class RepeatExpressionDTO {
	
	private int year;
	
	private int month;
	
	private int day;

	
	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}


	public int getMonth() {
		return month;
	}


	public void setMonth(int month) {
		this.month = month;
	}


	public int getDay() {
		return day;
	}


	public void setDay(int day) {
		this.day = day;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
