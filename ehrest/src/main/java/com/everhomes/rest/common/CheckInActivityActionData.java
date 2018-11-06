package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为CHECKIN_ACTIVITY跳转到活动帖子详情并自动签到
 * <li>forumId: 论坛ID
 * <li>topicId: 帖子ID</li>
 * <li>activityId: 活动ID</li>
 * <li>wechatSignup: 是否微信报名</li>
 * <li>categoryId: categoryId</li>
 * </ul>
 */
public class CheckInActivityActionData implements Serializable{
    private static final long serialVersionUID = 7502654058025166257L;
    //{"forumId": 1,"topicId":1,"activityId":1}  
    private Long forumId;
    private Long topicId;
    private Long activityId;
    private Byte wechatSignup;
    private Long categoryId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
