package com.everhomes.rest.user;

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
}
