package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>timeStep: 最短可预约时长-展示步长</li>
 * <li>beginTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * </ul>
 */
public class TimeIntervalDTO {

	private Double timeStep;
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

	public Double getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}
}
