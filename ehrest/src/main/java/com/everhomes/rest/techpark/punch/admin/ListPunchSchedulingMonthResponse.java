package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;


/**
 * <ul>
 * 列出一个月班次 
 * <li>schedulings: 结果{@link com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO}</li>
 * </ul>
 */	
public class ListPunchSchedulingMonthResponse { 
	@ItemType(PunchSchedulingDTO.class)
	private List<PunchSchedulingDTO> schedulings;

	public List<PunchSchedulingDTO> getSchedulings() {
		return schedulings;
	}

	public void setSchedulings(List<PunchSchedulingDTO> schedulings) {
		this.schedulings = schedulings;
	}
 
}
