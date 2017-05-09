// @formatter:off
package com.everhomes.rest.techpark.punch;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * <li>name：名称</li>
 * <li>description：描述</li>
 * <li>startEarlyTime：最早上班时间</li>
 * <li>startLateTime：最晚上班时间（如果是硬性工作时间，startLateTime = startEarlyTime）</li>
 * <li>endEarlyTime：最早下班班时间</li>
 * <li>endLateTime：最晚下班时间（如果是硬性工作时间，startLateTime = startEarlyTime）</li>
 * <li>workTime：每天工作时间 ,有由后台处理 (startLateTime - startEarlyTime)</li>
 * <li>createUid：创建人id</li>
 * <li>createTime：创建时间</li>
 * <li>updateUid：更新人id</li>
 * <li>updateTime：更新时间</li>
 * <li>punchTimesPerDay：每天打卡次数，2次或者4次)</li>
 * <li>daySplitTime：前一天与后一天的分界点</li>
 * </ul>
 */
public class PunchTimeRuleDTO {
	@NotNull
	private Long     id;
	private String name;
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	
	@NotNull
	private Long      startEarlyTime;
	@NotNull
	private Long      startLateTime;
	@NotNull
	private Long      endEarlyTime;
	@NotNull
	private Long noonLeaveTime;
	@NotNull
	private Long afternoonArriveTime;
	
	private Byte punchTimesPerDay;

	private Long daySplitTime;
	
	private String description;
	 
	 
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}


 

	public Long getStartEarlyTime() {
		return startEarlyTime;
	}



	public void setStartEarlyTime(Long startEarlyTime) {
		this.startEarlyTime = startEarlyTime;
	}



	public Long getStartLateTime() {
		return startLateTime;
	}



	public void setStartLateTime(Long startLateTime) {
		this.startLateTime = startLateTime;
	}



	public Long getEndEarlyTime() {
		return endEarlyTime;
	}



	public void setEndEarlyTime(Long endEarlyTime) {
		this.endEarlyTime = endEarlyTime;
	}



	public Long getNoonLeaveTime() {
		return noonLeaveTime;
	}



	public void setNoonLeaveTime(Long noonLeaveTime) {
		this.noonLeaveTime = noonLeaveTime;
	}



	public Long getAfternoonArriveTime() {
		return afternoonArriveTime;
	}



	public void setAfternoonArriveTime(Long afternoonArriveTime) {
		this.afternoonArriveTime = afternoonArriveTime;
	}



	public Byte getPunchTimesPerDay() {
		return punchTimesPerDay;
	}



	public void setPunchTimesPerDay(Byte punchTimesPerDay) {
		this.punchTimesPerDay = punchTimesPerDay;
	}

 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Long getDaySplitTime() {
		return daySplitTime;
	}



	public void setDaySplitTime(Long daySplitTime) {
		this.daySplitTime = daySplitTime;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}
}
