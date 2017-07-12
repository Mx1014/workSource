package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>TimeRule: 日期规则{@link com.everhomes.rest.techpark.punch.PunchTimeRuleDTO}</li>
 * <li>locationRule: 地点规则 {@link com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO}</li>
 * <li>wifiRule: wifi规则{@link com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO}</li>
 * <li>workdayRule: 排班规则{@link com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO}</li>
 * </ul>
 */
public class GetTargetPunchAllRuleResponse {
	
	private PunchTimeRuleDTO timeRule;
	
	private PunchLocationRuleDTO locationRule;
	
	private PunchWiFiRuleDTO wifiRule;
	
	private PunchWorkdayRuleDTO workdayRule;
	

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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


	public PunchTimeRuleDTO getTimeRule() {
		return timeRule;
	}


	public void setTimeRule(PunchTimeRuleDTO timeRule) {
		this.timeRule = timeRule;
	}

	
}
