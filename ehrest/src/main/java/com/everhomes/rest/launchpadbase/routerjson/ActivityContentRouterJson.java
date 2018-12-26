package com.everhomes.rest.launchpadbase.routerjson;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>forumId: forumId</li>
 *     <li>topicId: topicId</li>
 *     <li>categoryId: 类型</li>
 *     <li>是否支持微信报名，0-不支持，1-支持 参考  参考{@link com.everhomes.rest.activity.WechatSignupFlag }</li>
 * </ul>
 */
public class ActivityContentRouterJson {

    private Long forumId;
    private Long topicId;
    private Long categoryId;
    private Byte wechatSignup;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getWechatSignup() {
        return wechatSignup;
    }

    public void setWechatSignup(Byte wechatSignup) {
        this.wechatSignup = wechatSignup;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
