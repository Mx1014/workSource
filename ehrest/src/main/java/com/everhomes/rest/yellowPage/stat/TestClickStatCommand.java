package com.everhomes.rest.yellowPage.stat;

import com.everhomes.util.StringHelper;

public class TestClickStatCommand {
	private String startDate;
	private String endDate;
	
	@Override 
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}
