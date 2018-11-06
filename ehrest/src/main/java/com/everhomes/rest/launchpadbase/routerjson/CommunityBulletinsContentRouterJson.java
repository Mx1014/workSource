package com.everhomes.rest.launchpadbase.routerjson;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>forumId: forumId</li>
 *     <li>topicId: topicId</li>
 * </ul>
 */
public class CommunityBulletinsContentRouterJson {

    private Long forumId;
    private Long bulletinId;
    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(Long bulletinId) {
        this.bulletinId = bulletinId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
