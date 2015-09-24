// @formatter:off
package com.everhomes.techpark.punch;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>companyId：公司id</li>
 * <li>startEarlyTime：最早上班时间</li>
 * <li>startLateTime：最晚上班时间（如果是硬性工作时间，startLateTime = startEarlyTime）</li>
 * <li>endEarlyTime：最早下班班时间</li>
 * <li>endLateTime：最晚下班时间（如果是硬性工作时间，startLateTime = startEarlyTime）</li>
 * <li>workTime：每天工作时间 ,有由后台处理 (startLateTime - startEarlyTime)</li>
 * <li>locations：打卡地点的json数组</li>
 * </ul>
 */
public class CreatePunchRuleCommand {
	
	@NotNull
	private Long     companyId;
	
	@NotNull
	private String      startEarlyTime;
	@NotNull
	private String      startLateTime;
	@NotNull
	private String      endEarlyTime;
	@NotNull
	private String      locations;

	public CreatePunchRuleCommand() {
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getStartEarlyTime() {
		return startEarlyTime;
	}
	public void setStartEarlyTime(String startEarlyTime) {
		this.startEarlyTime = startEarlyTime;
	}
	public String getStartLateTime() {
		return startLateTime;
	}
	public void setStartLateTime(String startLateTime) {
		this.startLateTime = startLateTime;
	}
	public String getEndEarlyTime() {
		return endEarlyTime;
	}
	public void setEndEarlyTime(String endEarlyTime) {
		this.endEarlyTime = endEarlyTime;
	}
	public String getLocations() {
		return locations;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
