package com.everhomes.rest.techpark.rental.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>beginTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * </ul>
 */
public class TimeIntervalDTO {
	private Double beginTime;
	private Double endTime;

	public Double getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Double beginTime) {
		this.beginTime = beginTime;
	}

	public Double getEndTime() {
		return endTime;
	}

	public void setEndTime(Double endTime) {
		this.endTime = endTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
