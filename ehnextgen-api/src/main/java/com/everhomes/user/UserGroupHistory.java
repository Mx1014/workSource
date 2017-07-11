package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserGroupHistories;
import com.everhomes.util.StringHelper;

public class UserGroupHistory extends EhUserGroupHistories {
    /**
     * 
     */
    private static final long serialVersionUID = -5412673070829024986L;

    private String nickName;
    private String communityName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
