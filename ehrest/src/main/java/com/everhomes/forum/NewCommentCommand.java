// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

public class NewCommentCommand {
    private Long forumId;
    private Long topicId;
    
    private String contentType;
    private String content;
    
    // json encoded List<String> 
    private String attachmentListJson;
    
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

    public String getAttachmentListJson() {
        return attachmentListJson;
    }

    public void setAttachmentListJson(String attachmentListJson) {
        this.attachmentListJson = attachmentListJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
