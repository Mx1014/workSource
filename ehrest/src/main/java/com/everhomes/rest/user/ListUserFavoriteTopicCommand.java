package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <p>用户所收藏过的帖子或评论信息：</p>
 * <ul>
 * <li>communityId: 用户所在小区ID</li>
 * </ul>
 */
public class ListUserFavoriteTopicCommand {
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
