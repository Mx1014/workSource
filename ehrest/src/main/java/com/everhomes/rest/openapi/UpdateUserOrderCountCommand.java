package com.everhomes.rest.openapi;

import javax.validation.constraints.NotNull;

public class UpdateUserOrderCountCommand {
	@NotNull
    private Long userId;
	@NotNull
	private Integer orderCount;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
}
