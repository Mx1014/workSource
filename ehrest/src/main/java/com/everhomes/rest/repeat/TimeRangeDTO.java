package com.everhomes.rest.repeat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>startTime: 开始时间  HH:mm:ss</li>
 *  <li>duration: 时间跨度</li>
 * </ul>
 */
public class TimeRangeDTO {

	private String startTime;
	
	private String duration;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
