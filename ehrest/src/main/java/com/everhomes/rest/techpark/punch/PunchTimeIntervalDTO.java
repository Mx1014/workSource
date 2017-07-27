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
 * <li>punchOrganizationId：考勤组id </li>
 * <li>punchRuleId： 规则id</li>
 * <li>timeRuleId： 时间规则id</li>
 * <li>arriveTime： 上班时间</li> 
 * <li>workTime： 下班时间</li>
 * </ul>
 */
public class PunchTimeIntervalDTO {
	@NotNull
	private Long id;  
    private Long punchOrganizationId;
    private Long punchRuleId;
    private Long timeRuleId;
    private Long arriveTime;
    private Long leaveTime; 
 
	  
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
 

	public Long getPunchOrganizationId() {
		return punchOrganizationId;
	}


	public void setPunchOrganizationId(Long punchOrganizationId) {
		this.punchOrganizationId = punchOrganizationId;
	}


	public Long getPunchRuleId() {
		return punchRuleId;
	}


	public void setPunchRuleId(Long punchRuleId) {
		this.punchRuleId = punchRuleId;
	}


	public Long getTimeRuleId() {
		return timeRuleId;
	}


	public void setTimeRuleId(Long timeRuleId) {
		this.timeRuleId = timeRuleId;
	}


	public Long getArriveTime() {
		return arriveTime;
	}


	public void setArriveTime(Long arriveTime) {
		this.arriveTime = arriveTime;
	}
 
	public Long getLeaveTime() {
		return leaveTime;
	}


	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}

 
}
