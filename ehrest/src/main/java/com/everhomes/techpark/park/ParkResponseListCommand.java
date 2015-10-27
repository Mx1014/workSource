package com.everhomes.techpark.park;

public class ParkResponseListCommand {
	
	private Integer pageOffset;
	
	private Integer pageSize;
	
	private String rechargePhone;
	
	public String getRechargePhone() {
		return rechargePhone;
	}

	public void setRechargePhone(String rechargePhone) {
		this.rechargePhone = rechargePhone;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	} 
	
}
