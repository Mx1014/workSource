package com.everhomes.rest.enterpriseApproval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>restId: 请假id</li>
 * <li>restName: 请假类型</li>
 * <li>startTime: 开始时间 格式为:yy-MM-dd HH:mm</li>
 * <li>endTime: 结束时间 格式为:yy-MM-dd HH:mm</li>
 * <li>timeUnit: 请假时长的单位，{@link com.everhomes.rest.approval.ApprovalCategoryTimeUnit}</li>
 * <li>duration: 请假时长</li>
 * </ul>
 */
public class ComponentAskForLeaveValue {

	private Long restId;

	private String restName;

	private String startTime;

	private String endTime;

	private String timeUnit;

	private Double duration;

	public String getStartTime() {
		return startTime;
	}

	public Long getRestId() {
		return restId;
	}

	public void setRestId(Long restId) {
		this.restId = restId;
	}

	public String getRestName() {
		return restName;
	}

	public void setRestName(String restName) {
		this.restName = restName;
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

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
