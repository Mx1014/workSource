package com.everhomes.rest.openapi;

import javax.validation.constraints.NotNull;

public class UpdateUserCouponCountCommand {
	@NotNull
    private Long userId;
	@NotNull
	private Integer couponCount;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}
}
