package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class FindTokenByUserIdCommand {
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
