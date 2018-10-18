package com.everhomes.rest.launchpadbase.routerjson;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>forumId: forumId</li>
 *     <li>topicId: topicId</li>
 * </ul>
 */
public class ActivityContentRouterJson {

    private Long forumId;
    private Long topicId;

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
