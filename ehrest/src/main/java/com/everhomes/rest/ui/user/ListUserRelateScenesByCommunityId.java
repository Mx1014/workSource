package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *   <li>communityId:场景Id	</li>
 * </ul>
 *
 */
public class ListUserRelateScenesByCommunityId {
    private Long communityId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}
