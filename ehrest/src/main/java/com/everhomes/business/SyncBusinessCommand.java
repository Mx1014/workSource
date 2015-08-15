package com.everhomes.business;



import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 当前用户id</li>
 * </ul>
 */

public class SyncBusinessCommand extends BusinessCommand{
    private Long    userId;
   
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
