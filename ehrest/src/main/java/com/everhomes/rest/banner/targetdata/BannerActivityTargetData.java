package com.everhomes.rest.banner.targetdata;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>categoryId: 活动分类id</li>
 *     <li>name: 活动应用名称</li>
 *     <li>activityId: 活动id,可选参数</li>
 *     <li>subject: 活动主题</li>
 *     <li>postId: 帖子id</li>
 *     <li>forumId: 论坛id</li>
 * </ul>
 */
public class BannerActivityTargetData {

    private Long categoryId;
    private String name;
    private Long activityId;
    private String subject;
    private Long postId;
    private Long forumId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
