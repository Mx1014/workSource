package com.everhomes.rest.business;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户ID</li>
 * </ul>
 */

public class SyncUserAddShopStatusCommand{
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
