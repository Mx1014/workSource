package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>startTime: 开始时间</li>
 *  <li>endTime: 结束时间</li>
 * </ul>
 *
 */
public class GetVideoConfAccountStatisticsCommand {
	
	private Long startTime;
	
	private Long endTime;

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
