package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

public class FloorRangeDTO {
	
	private Integer maxFloor;

	public Integer getMaxFloor() {
		return maxFloor;
	}

	public void setMaxFloor(Integer maxFloor) {
		this.maxFloor = maxFloor;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
