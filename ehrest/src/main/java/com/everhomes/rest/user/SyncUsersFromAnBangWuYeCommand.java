package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 *
 * 是否是全量（0：不是;1 是）
 */
public class SyncUsersFromAnBangWuYeCommand {
    private Integer isAll;

    public Integer getIsAll() {
        return isAll;
    }

    public void setIsAll(Integer isAll) {
        this.isAll = isAll;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
