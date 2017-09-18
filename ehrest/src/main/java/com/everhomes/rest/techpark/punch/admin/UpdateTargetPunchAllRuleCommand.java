package com.everhomes.rest.techpark.punch.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>ownerType：所属对象类型organization</li>
 * <li>ownerId：所属对象id</li>
 * <li>targetType：映射目标类型(规则是设置给谁的) organization/user</li>
 * <li>targetId：映射目标 id</li> 
 * <li>TimeRule: 日期规则{@link com.everhomes.rest.techpark.punch.PunchTimeRuleDTO}</li>
 * <li>locationRule: 地点规则 {@link com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO}</li>
 * <li>wifiRule: wifi规则{@link com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO}</li>
 * <li>workdayRule: 排班规则{@link com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO}</li>
 * </ul>
 */
public class UpdateTargetPunchAllRuleCommand {

	
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	
	private String targetType;
	private Long targetId;
	
	private PunchTimeRuleDTO TimeRule;
	
	private PunchLocationRuleDTO locationRule;
	
	private PunchWiFiRuleDTO wifiRule;
	
	private PunchWorkdayRuleDTO workdayRule;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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


	public PunchTimeRuleDTO getTimeRule() {
		return TimeRule;
	}


	public void setTimeRule(PunchTimeRuleDTO timeRule) {
		TimeRule = timeRule;
	}


	public PunchLocationRuleDTO getLocationRule() {
		return locationRule;
	}


	public void setLocationRule(PunchLocationRuleDTO locationRule) {
		this.locationRule = locationRule;
	}


	public PunchWiFiRuleDTO getWifiRule() {
		return wifiRule;
	}


	public void setWifiRule(PunchWiFiRuleDTO wifiRule) {
		this.wifiRule = wifiRule;
	}


	public PunchWorkdayRuleDTO getWorkdayRule() {
		return workdayRule;
	}


	public void setWorkdayRule(PunchWorkdayRuleDTO workdayRule) {
		this.workdayRule = workdayRule;
	}
	
}
