package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>startTime: 开始时间 格式为:yy-MM-dd HH:mm</li>
 * <li>endTime: 结束时间 格式为:yy-MM-dd HH:mm</li>
 * <li>duration: 外出时长</li>
 * </ul>
 */
public class PostApprovalFormGoOutValue {

	private String startTime;

	private String endTime;

	private String duration;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
