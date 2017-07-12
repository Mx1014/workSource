package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;


/**
 * <ul>
 *  
 * <li>schedulings: 每日班次--如果没有排班也要传,ruleid给我null{@link com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO}</li>
 * </ul>
 */	
public class UpdatePunchSchedulingMonthCommand { 
	@ItemType(PunchSchedulingDTO.class)
	private List<PunchSchedulingDTO> schedulings;

	public List<PunchSchedulingDTO> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<PunchSchedulingDTO> schedulings) {
		this.schedulings = schedulings;
	}
 
}
