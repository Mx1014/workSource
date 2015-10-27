package com.everhomes.techpark.park;

public class ParkingChargeDTO {
	
	private Long id;
	
	private Byte months;
	
	private Integer amount;
	
	private Long enterpriseCommunityId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getMonths() {
		return months;
	}

	public void setMonths(Byte months) {
		this.months = months;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}
	

}
