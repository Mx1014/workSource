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
 * <li>punchTimeIntervals：上班时间段</li> 
 * <li>flexTime：弹性时间段</li>
 * <li>punchLimitTime：提前多少可以打卡的打卡限制时间</li>
 * <li>noonLeaveTime：午休开始时间</li>
 * <li>afternoonArriveTime：午休结束时间</li> 
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
	
	@ItemType(PunchTimeIntervalDTO.class)
	private List<PunchTimeIntervalDTO> punchTimeIntervals;

    private Long flexTime;
    
    private Long punchLimitTime;
    
	@NotNull
	private Long noonLeaveTime;
	@NotNull
	private Long afternoonArriveTime;
	 
	private Long daySplitTime;
	
	private String description;
	  

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


	public Long getPunchLimitTime() {
		return punchLimitTime;
	}


	public void setPunchLimitTime(Long punchLimitTime) {
		this.punchLimitTime = punchLimitTime;
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
 
}
