// @formatter:off
package com.everhomes.forum;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class NewTopicCommand {
    @NotNull
    private Long forumId;
    
    private Long categoryId;
    
    private Byte visibilityScope;
    private Long visibilityScopeId;
    
    @NotNull
    private String subject;
    
    @NotNull
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

    public Byte getVisibilityScope() {
        return visibilityScope;
    }

    public void setVisibilityScope(Byte visibilityScope) {
        this.visibilityScope = visibilityScope;
    }

    public Long getVisibilityScopeId() {
        return visibilityScopeId;
    }

    public void setVisibilityScopeId(Long visibilityScopeId) {
        this.visibilityScopeId = visibilityScopeId;
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
