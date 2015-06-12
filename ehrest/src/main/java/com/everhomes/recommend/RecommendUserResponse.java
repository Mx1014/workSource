package com.everhomes.recommend;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.user.UserInfo;
import com.everhomes.util.StringHelper;


/**
 * 获取推荐的用户信息
 * @author janson
 *
 */
public class RecommendUserResponse {
    @ItemType(UserInfo.class)
    private List<UserInfo> users;
    
    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
