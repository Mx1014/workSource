package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;


/**
 * <ul>
 * 列出一个月班次 
 * <li>schedulings: 结果{@link com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO}</li>
 * <li>timeRules: 结果{@link com.everhomes.rest.techpark.punch.PunchTimeRuleDTO}</li>
 * </ul>
 */	
public class ListPunchSchedulingMonthResponse { 
	@ItemType(PunchSchedulingDTO.class)
	private List<PunchSchedulingDTO> schedulings;

	@ItemType(PunchTimeRuleDTO.class)
	private List<PunchTimeRuleDTO> timeRules;
	
	public List<PunchSchedulingDTO> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<PunchSchedulingDTO> schedulings) {
		this.schedulings = schedulings;
	}

	public List<PunchTimeRuleDTO> getTimeRules() {
		return timeRules;
	}

	public void setTimeRules(List<PunchTimeRuleDTO> timeRules) {
		this.timeRules = timeRules;
	}
 
}
