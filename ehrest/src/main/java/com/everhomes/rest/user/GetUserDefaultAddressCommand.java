package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class GetUserDefaultAddressCommand {
	@NotNull
    private Long userId;

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
