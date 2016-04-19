package com.everhomes.rest.business;

public class UpdateReceivedCouponCountCommand {
	private Long userId;
	private Integer count;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
