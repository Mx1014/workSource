package com.everhomes.rest.recommend;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.StringHelper;


/**
 * 获取推荐的用户信息
 * @author janson
 *
 */
public class RecommendUserResponse {
    @ItemType(RecommendUserInfo.class)
    private List<RecommendUserInfo> users;
    
    public List<RecommendUserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<RecommendUserInfo> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
