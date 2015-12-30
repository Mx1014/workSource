// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>topicId: 原帖子ID</li>
 * <li>subject: 原帖子标题</li>
 * <li>targetForumId: 原帖所在论坛ID</li>
 * <li>contentUri: 内容图片URI</li>
 * <li>contentUrl: 内容图片URL</li>
 * </ul>
 */
public class ForwardTopicCommand {
    private Long topicId;
    
    private Long forumId;
    
    private String subject;
    
    private String contentUri;
    
    private String contentUrl;
    
    public ForwardTopicCommand() {
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
