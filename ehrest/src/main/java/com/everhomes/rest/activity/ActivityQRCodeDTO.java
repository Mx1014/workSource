// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

public class ActivityQRCodeDTO {
    private Long forumId;
    private Long topicId;
    private Long activityId;
    private Byte wechatSignup;
    private Long categoryId;

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

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Byte getWechatSignup() {
        return wechatSignup;
    }

    public void setWechatSignup(Byte wechatSignup) {
        this.wechatSignup = wechatSignup;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
