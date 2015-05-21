// @formatter:off
package com.everhomes.forum;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>contentCategory: 内容类型ID，含类和子类</li>
 * <li>actionCategory: 操作类型ID，如拼车中的“我搭车”、“我开车”</li>
 * <li>visibilityScope: 可见性范围类型，{@link com.everhomes.visibility.VisibilityScope}</li>
 * <li>visibilityScopeId: 可见性类型对应的ID</li>
 * <li>longitude: 帖子内容涉及到的经度如活动</li>
 * <li>latitude: 帖子内容涉及到的纬度如活动</li>
 * <li>subject: 帖子标题</li>
 * <li>contentType: 帖子内容类型，{@link com.everhomes.forum.PostContentType}</li>
 * <li>content: 帖子内容</li>
 * <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.app.AppConstants}</li>
 * <li>embeddedId: 内嵌对象对应的ID</li>
 * <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 * <li>isForwarded: 是否是转发帖的标记</li>
 * <li>attachments: 图片、语音、视频等附件信息，参考{@link com.everhomes.forum.AttachmentDescriptor}</li>
 * </ul>
 */
public class NewTopicCommand {
    @NotNull
    private Long forumId;

    private Long contentCategory;
    
    private Long actionCategory;
    
    private Byte visibilityScope;

    private Long visibilityScopeId;
    
    private Double longitude;
    
    private Double latitude;
    
    @NotNull
    private String subject;
    
    @NotNull
    private String contentType;
    
    @NotNull
    private String content;
    
    private long embeddedAppId;
    
    private Long embeddedId;
    
    // json encoded List<String> 
    private String embeddedJson;
    
    private Byte isForwarded;
    
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
    
    public NewTopicCommand() {
    }
    
    public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
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

    public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public long getEmbeddedAppId() {
        return embeddedAppId;
    }

    public void setEmbeddedAppId(long embeddedAppId) {
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

	public Byte getIsForwarded() {
		return isForwarded;
	}

	public void setIsForwarded(Byte isForwarded) {
		this.isForwarded = isForwarded;
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
