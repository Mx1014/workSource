package com.everhomes.rest.organization.pm.reportForm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class ApartmentBriefStaticsDTO {
	
	private String address;
	private BigDecimal areaSize;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(BigDecimal areaSize) {
		this.areaSize = areaSize;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
