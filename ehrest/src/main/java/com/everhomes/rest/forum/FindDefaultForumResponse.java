// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>defaultForum: 园区默认论坛 {@link com.everhomes.rest.forum.ForumDTO}</li>
 *     <li>feedbackForum: 意见反馈默认论坛 {@link com.everhomes.rest.forum.ForumDTO}</li>
 * </ul>
 */
public class FindDefaultForumResponse {

    private ForumDTO defaultForum;
    private ForumDTO feedbackForum;

    public ForumDTO getDefaultForum() {
        return defaultForum;
    }

    public void setDefaultForum(ForumDTO defaultForum) {
        this.defaultForum = defaultForum;
    }

    public ForumDTO getFeedbackForum() {
        return feedbackForum;
    }

    public void setFeedbackForum(ForumDTO feedbackForum) {
        this.feedbackForum = feedbackForum;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
