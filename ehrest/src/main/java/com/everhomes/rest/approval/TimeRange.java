// @formatter:off
package com.everhomes.rest.approval;

import java.sql.Timestamp;

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
	private Timestamp fromTime;
	private Timestamp endTime;
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

	public Timestamp getFromTime() {
		return fromTime;
	}

	public void setFromTime(Timestamp fromTime) {
		this.fromTime = fromTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

}
