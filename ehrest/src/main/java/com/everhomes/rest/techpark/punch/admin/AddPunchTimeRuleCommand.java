// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>ownerType: 填organization</li>
 * <li>ownerId：公司id</li>
 * <li>targetType: 填organization/user</li>
 * <li>targetId：对应设置目标的id比如机构比如人的id</li>
 * 
 * <li>name：名称</li>
 * <li>description：描述</li>
 * <li>startEarlyTime：最早上班时间(上班时间开始)</li>
 * <li>startLateTime：最晚上班时间(上班时间结束)</li>
 * <li>endEarlyTime：最早下班班时间</li>
 * <li>noonLeaveTime：中午下班时间 (休息时间开始)</li>
 * <li>afternoonArriveTime： 下午上班时间(休息时间结束)</li>
 * <li>punchTimesPerDay：每天打卡次数，2次或者4次(休息时间需要考勤 就是4次)</li>
 * <li>daySplitTime：前一天与后一天的分界点</li>
 * </ul>
 */
public class AddPunchTimeRuleCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;

	private String targetType;
	private Long targetId;

	private String description;
	private String name;
	@NotNull
	private Long startEarlyTime;
	@NotNull
	private Long startLateTime;
	@NotNull
	private Long endEarlyTime;
	@NotNull
	private Long noonLeaveTime;
	@NotNull
	private Long afternoonArriveTime;
	@NotNull
	private Byte punchTimesPerDay;

	private Long daySplitTime;
	
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

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
