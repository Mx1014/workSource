// @formatter:off
package com.everhomes.forum;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>帖子或评论信息：</p>
 * <ul>
 * <li>id: 帖子或评论ID</li>
 * <li>uuid: 帖子或评论UUID</li>
 * <li>parentPostId: 帖子或评论的父亲ID</li>
 * <li>forumId: 论坛ID</li>
 * <li>creatorUid: 创建者ID</li>
 * <li>creatorNickName: 创建者在圈内的昵称</li>
 * <li>creatorAvatar: 创建者在圈内的头像URI</li>
 * <li>creatorAvatarUrl: 创建者在圈内的头像URL</li>
 * <li>creatorAdminFlag: 创建者是否为圈的管理员</li>
 * <li>creatorTag: 创建者标签，参考{@link com.everhomes.forum.PostEntityTag}</li>
 * <li>targetTag: 创建者标签，参考{@link com.everhomes.forum.PostEntityTag}</li>
 * <li>contentCategory: 内容类型ID，含类和子类</li>
 * <li>actionCategory: 操作类型ID，如拼车中的“我搭车”、“我开车”</li>
 * <li>visibleRegionType: 区域范围类型，{@link com.everhomes.visibility.VisibleRegionType}</li>
 * <li>visibleRegionId: 区域范围类型对应的ID</li>
 * <li>visibleRegionName: 区域范围对应的名称</li>
 * <li>longitude: 帖子或评论内容涉及到的经度如活动</li>
 * <li>latitude: 帖子或评论内容涉及到的纬度如活动</li>
 * <li>subject: 帖子或评论标题</li>
 * <li>contentType: 帖子或评论内容类型，{@link com.everhomes.forum.PostContentType}</li>
 * <li>content: 帖子或评论内容</li>
 * <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.app.AppConstants}</li>
 * <li>embeddedId: 内嵌对象ID</li>
 * <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 * <li>isForwarded: 是否是转发帖的标记</li>
 * <li>childCount: 孩子数目，如帖子下的评论数目</li>
 * <li>forwardCount: 帖子或评论的转发数目</li>
 * <li>likeCount: 帖子或评论赞的数目</li>
 * <li>dislikeCount: 帖子或评论踩的数目</li>
 * <li>viewCount: 浏览的数目</li>
 * <li>updateTime: 帖子或评论更新时间</li>
 * <li>createTime: 帖子或评论创建时间</li>
 * <li>attachments: 帖子或评论的附件信息，参见{@link com.everhomes.forum.AttachmentDTO}</li>
 * <li>assignedFlag: 是否推荐帖，参见{@link com.everhomes.forum.PostAssignedFlag}</li>
 * <li>likeFlag: 是否推荐帖，参见{@link com.everhomes.forum.PostLikeFlag}</li>
 * <li>shareUrl: 分享链接</li>
 * </ul>
 */
public class PostDTO {
	private Long id;
	
	private String uuid;
	
	private Long parentPostId;
	
    private Long forumId;
    
    private Long creatorUid;
    
    private String creatorNickName;
    
    private String creatorAvatar;
    
    private String creatorAvatarUrl;
    
    private Byte creatorAdminFlag;
    
    private String creatorTag;
    
    private String targetTag;    

    private Long contentCategory;
    
    private Long actionCategory;
    
    private Byte visibleRegionType;

    private Long visibleRegionId;
    
    private String visibleRegionName;
    
    private Double longitude;
    
    private Double latitude;
    
    private String subject;
    
    private String contentType;
    
    private String content;
    
    private Long embeddedAppId;
    
    private Long embeddedId;
    
    // json encoded List<String> 
    private String embeddedJson;
    
    private Byte isForwarded;
    
    private Long childCount;
    
    private Long forwardCount;
    
    private Long likeCount;
    
    private Long dislikeCount;
    
    private Long viewCount;
    
    private Timestamp updateTime;
    
    private Timestamp createTime;
    
    @ItemType(AttachmentDTO.class)
    private List<AttachmentDTO> attachments;
    
    private Byte assignedFlag;
    
    private String forumName;
    
    private Byte likeFlag;
    
    private String shareUrl;

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getParentPostId() {
		return parentPostId;
	}

	public void setParentPostId(Long parentPostId) {
		this.parentPostId = parentPostId;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorNickName() {
        return creatorNickName;
    }

    public void setCreatorNickName(String creatorNickName) {
        this.creatorNickName = creatorNickName;
    }

    public String getCreatorAvatar() {
        return creatorAvatar;
    }

    public void setCreatorAvatar(String creatorAvatar) {
        this.creatorAvatar = creatorAvatar;
    }

    public String getCreatorAvatarUrl() {
        return creatorAvatarUrl;
    }

    public void setCreatorAvatarUrl(String creatorAvatarUrl) {
        this.creatorAvatarUrl = creatorAvatarUrl;
    }

    public Byte getCreatorAdminFlag() {
        return creatorAdminFlag;
    }

    public void setCreatorAdminFlag(Byte creatorAdminFlag) {
        this.creatorAdminFlag = creatorAdminFlag;
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

    public String getVisibleRegionName() {
        return visibleRegionName;
    }

    public void setVisibleRegionName(String visibleRegionName) {
        this.visibleRegionName = visibleRegionName;
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

	public Long getChildCount() {
		return childCount;
	}

	public void setChildCount(Long childCount) {
		this.childCount = childCount;
	}

	public Long getForwardCount() {
		return forwardCount;
	}

	public void setForwardCount(Long forwardCount) {
		this.forwardCount = forwardCount;
	}

	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	public Long getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(Long dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Byte getAssignedFlag() {
        return assignedFlag;
    }

    public void setAssignedFlag(Byte assignedFlag) {
        this.assignedFlag = assignedFlag;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public Byte getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(Byte likeFlag) {
        this.likeFlag = likeFlag;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
