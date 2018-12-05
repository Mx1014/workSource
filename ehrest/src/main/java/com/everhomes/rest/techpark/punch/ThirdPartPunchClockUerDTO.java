package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId：打卡用户id 必填</li> 
 * <li>punchTime：打卡时间 如果不填就用请求的时间做打卡时间</li> 
 * </ul>
 */
public class ThirdPartPunchClockUerDTO {
	private Long userId;
	private Long punchTime;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPunchTime() {
		return punchTime;
	}

	public void setPunchTime(Long punchTime) {
		this.punchTime = punchTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
