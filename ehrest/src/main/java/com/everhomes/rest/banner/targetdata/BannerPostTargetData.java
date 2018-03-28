package com.everhomes.rest.banner.targetdata;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>forumEntryId: 论坛入口id</li>
 *     <li>name: 应用入口名称</li>
 *     <li>postId: 帖子id</li>
 *     <li>forumId: 论坛id</li>
 *     <li>subject: 帖子主题</li>
 * </ul>
 */
public class BannerPostTargetData {

    private Long forumEntryId;
    private String name;
    private Long postId;
    private Long forumId;
    private String subject;

    public Long getForumEntryId() {
        return forumEntryId;
    }

    public void setForumEntryId(Long forumEntryId) {
        this.forumEntryId = forumEntryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
