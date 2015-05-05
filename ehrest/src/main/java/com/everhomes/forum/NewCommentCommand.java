// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>topicId: 帖子ID</li>
 * <li>content_type: 帖子内容类型，{@link com.everhomes.forum.PostContentType}</li>
 * <li>content: 帖子内容</li>
 * </ul>
 */
public class NewCommentCommand {
    private Long forumId;
    
    private Long topicId;
    
    private String contentType;
    
    private String content;
    
    public NewCommentCommand() {
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
