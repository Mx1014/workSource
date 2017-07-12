package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 获取用户昵称
 * <ul>
 * <li>uid: 用户id</li>
 * </ul>
 */
public class GetUserNickNameCommand {

    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
