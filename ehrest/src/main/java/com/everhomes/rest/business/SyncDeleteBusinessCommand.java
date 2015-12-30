package com.everhomes.rest.business;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 商家ID</li>
 * <li>userId: 用户ID</li>
 * </ul>
 */

public class SyncDeleteBusinessCommand{
    @NotNull
    private String id;
    @NotNull
    private Long userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
