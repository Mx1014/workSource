// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>type: 类型，参考{@link com.everhomes.rest.approval.TimeRangeType}</li>
 * <li>fromTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>actualResult: 实际时长</li>
 * </ul>
 */
public class TimeRange {
	private Byte type;
	private Long fromTime;
	private Long endTime;
	private String actualResult;

	public String getActualResult() {
		return actualResult;
	}

	public void setActualResult(String actualResult) {
		this.actualResult = actualResult;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Long getFromTime() {
		return fromTime;
	}

	public void setFromTime(Long fromTime) {
		this.fromTime = fromTime;
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
