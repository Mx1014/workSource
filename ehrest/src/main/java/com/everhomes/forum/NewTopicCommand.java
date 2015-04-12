// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

public class NewTopicCommand {
    private Long forumId;
    
    private Long categoryId;
    
    private Byte visibleRegionScope;
    private Long visibleRegionId;
    private Long visibleRegionPath;
    
    private String subject;
    private String content;
    
    // json encoded List<String> 
    private String attachmentListJson;
    
    private Byte isForwarded;
    
    public NewTopicCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getVisibleRegionScope() {
        return visibleRegionScope;
    }

    public void setVisibleRegionScope(Byte visibleRegionScope) {
        this.visibleRegionScope = visibleRegionScope;
    }

    public Long getVisibleRegionId() {
        return visibleRegionId;
    }

    public void setVisibleRegionId(Long visibleRegionId) {
        this.visibleRegionId = visibleRegionId;
    }

    public Long getVisibleRegionPath() {
        return visibleRegionPath;
    }

    public void setVisibleRegionPath(Long visibleRegionPath) {
        this.visibleRegionPath = visibleRegionPath;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public Byte getIsForwarded() {
        return isForwarded;
    }

    public void setIsForwarded(Byte isForwarded) {
        this.isForwarded = isForwarded;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
