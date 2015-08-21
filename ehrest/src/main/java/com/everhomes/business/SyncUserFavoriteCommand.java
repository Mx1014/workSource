package com.everhomes.business;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户ID</li>
 * <li>id: 店铺ID</li>
 * </ul>
 */

public class SyncUserFavoriteCommand{
    @NotNull
    private Long userId;
    @NotNull
    private String id;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
