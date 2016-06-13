package com.everhomes.rest.openapi;

import javax.validation.constraints.NotNull;

public class UserCouponsCommand {
	
	@NotNull
    private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
