// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>topicId: 帖子ID</li>
 * </ul>
 */
public class PublishTopicCommand {

    private Long topicId;
    
    public PublishTopicCommand() {
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
