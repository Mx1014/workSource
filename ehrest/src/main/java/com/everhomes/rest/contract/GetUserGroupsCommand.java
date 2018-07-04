package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2018/1/26.
 */
public class GetUserGroupsCommand {

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
