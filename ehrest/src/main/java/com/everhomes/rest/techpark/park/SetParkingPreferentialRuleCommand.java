package com.everhomes.rest.techpark.park;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>startTime: 活动开始时间  字符串格式：yyyyMMdd</li>
 *  <li>endTime: 活动结束时间  字符串格式： yyyyMMdd</li>
 *  <li>range: 前多少位可享受活动</li>
 * </ul>
 *
 */
public class SetParkingPreferentialRuleCommand {
	
	private String startTime;
	
	private String endTime;
	
	private String range;

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

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
