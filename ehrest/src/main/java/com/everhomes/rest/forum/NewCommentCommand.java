// @formatter:off
package com.everhomes.rest.forum;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>topicId: 帖子ID</li>
 * <li>parentId: 回复的评论帖子ID</li>
 * <li>parentCommentId: 父评论的ID</li>
 * <li>content_type: 帖子内容类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 * <li>content: 帖子内容</li>
 * <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.rest.app.AppConstants}</li>
 * <li>embeddedId: 内嵌对象对应的ID</li>
 * <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 * <li>attachments: 图片、语音、视频等附件信息，参考{@link com.everhomes.rest.forum.AttachmentDescriptor}</li>
 * </ul>
 */
public class NewCommentCommand {
    private Long forumId;
    
    private Long topicId;
    
    private Long parentId;
    
    private Long parentCommentId;
    
    private String contentType;
    
    private String content;
    
    private Long embeddedAppId;
    
    private Long embeddedId;
    
    private String embeddedJson;
    
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
    
    public NewCommentCommand() {
    }

    public Long getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(Long parentCommentId) {
		this.parentCommentId = parentCommentId;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

    public Long getEmbeddedAppId() {
        return embeddedAppId;
    }

    public void setEmbeddedAppId(Long embeddedAppId) {
        this.embeddedAppId = embeddedAppId;
    }

    public Long getEmbeddedId() {
        return embeddedId;
    }

    public void setEmbeddedId(Long embeddedId) {
        this.embeddedId = embeddedId;
    }

    public String getEmbeddedJson() {
        return embeddedJson;
    }

    public void setEmbeddedJson(String embeddedJson) {
        this.embeddedJson = embeddedJson;
    }

    public List<AttachmentDescriptor> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDescriptor> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
