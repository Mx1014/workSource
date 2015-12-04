package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * userId：用户id
 *
 */
public class VerifyVideoConfAccountCommand {
	
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
