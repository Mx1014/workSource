package com.everhomes.rest.user;


import com.everhomes.util.StringHelper;


/**
 * <p>用户所收藏过的活动：</p>
 * <ul>
 * <li>communityId: 用户所在小区或园区ID</li>
 * </ul>
 */
public class ListUserFavoriteActivityCommand {
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
