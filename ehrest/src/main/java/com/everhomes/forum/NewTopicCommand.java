// @formatter:off
package com.everhomes.forum;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>creatorTag: 创建者标签，参考{@link com.everhomes.forum.PostEntityTag}</li>
 * <li>targetTag: 创建者标签，参考{@link com.everhomes.forum.PostEntityTag}</li>
 * <li>contentCategory: 内容类型ID，含类和子类</li>
 * <li>actionCategory: 操作类型ID，如拼车中的“我搭车”、“我开车”</li>
 * <li>visibleRegionType: 区域范围类型，{@link com.everhomes.visibility.VisibleRegionType}</li>
 * <li>visibleRegionId: 区域范围类型对应的ID</li>
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
 * <li>privateFlag: 帖子是否公开标记，参考{@link com.everhomes.forum.PostPrivacy}</li>
 * </ul>
 */
public class NewTopicCommand {
    @NotNull
    private Long forumId;
    
    private String creatorTag;
    
    private String targetTag;    
    
    private Long contentCategory;
    
    private Long actionCategory;
    
    private Byte visibleRegionType;

    private Long visibleRegionId;
    
    private Double longitude;
    
    private Double latitude;
    
    @NotNull
    private String subject;
    
    //去掉非空验证
    private String contentType;
    
    //去掉非空验证
    private String content;
    
    private Long embeddedAppId;
    
    private Long embeddedId;
    
    // json encoded List<String> 
    private String embeddedJson;
    
    private Byte isForwarded;
    
    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;
    
    private Byte privateFlag;
    
    public NewTopicCommand() {
    }
    
    public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

    public String getCreatorTag() {
        return creatorTag;
    }

    public void setCreatorTag(String creatorTag) {
        this.creatorTag = creatorTag;
    }

    public String getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(String targetTag) {
        this.targetTag = targetTag;
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

    public Byte getVisibleRegionType() {
        return visibleRegionType;
    }

    public void setVisibleRegionType(Byte visibleRegionType) {
        this.visibleRegionType = visibleRegionType;
    }

    public Long getVisibleRegionId() {
        return visibleRegionId;
    }

    public void setVisibleRegionId(Long visibleRegionId) {
        this.visibleRegionId = visibleRegionId;
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

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
