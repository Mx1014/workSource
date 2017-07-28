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
 * <li>name : 名称</li>  
 * <li>description : 描述</li>  
 * <li>openWeekday : 一周开放日期: 7位二进制，0000000每一位表示星期7123456</li>   
 * <li>punchTimeIntervals：上班时间段</li> 
 * <li>flexTime：弹性时间段 {@link com.everhomes.rest.techpark.punch.PunchTimeIntervalDTO}</li> 
 * <li>noonLeaveTime：午休开始时间 (只有一段打卡可以设置)</li>
 * <li>afternoonArriveTime：午休结束时间 (只有一段打卡可以设置)</li> 
 * <li>beginPunchTime：允许开始打开时间--上班时间前多久可以打开</li> 
 * <li>endPunchTime: 允许结束打卡时间--下班时间后多久可以打卡 (排班制字段)</li>  
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
	
	@ItemType(PunchTimeIntervalDTO.class)
	private List<PunchTimeIntervalDTO> punchTimeIntervals;

    private Long flexTime;
    
	@NotNull
	private Long noonLeaveTime;
	@NotNull
	private Long afternoonArriveTime;
	 
	private Long daySplitTime;
	
	private String description;
	  
	private Long beginPunchTime;
	
	private Long endPunchTime;
	
	private String openWeekday;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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


	public List<PunchTimeIntervalDTO> getPunchTimeIntervals() {
		return punchTimeIntervals;
	}


	public void setPunchTimeIntervals(List<PunchTimeIntervalDTO> punchTimeIntervals) {
		this.punchTimeIntervals = punchTimeIntervals;
	}


	public Long getFlexTime() {
		return flexTime;
	}


	public void setFlexTime(Long flexTime) {
		this.flexTime = flexTime;
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


	public Long getBeginPunchTime() {
		return beginPunchTime;
	}


	public void setBeginPunchTime(Long beginPunchTime) {
		this.beginPunchTime = beginPunchTime;
	}


	public Long getEndPunchTime() {
		return endPunchTime;
	}


	public void setEndPunchTime(Long endPunchTime) {
		this.endPunchTime = endPunchTime;
	}


	public String getOpenWeekday() {
		return openWeekday;
	}


	public void setOpenWeekday(String openWeekday) {
		this.openWeekday = openWeekday;
	}
 
}
