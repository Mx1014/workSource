// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

/**
 * <p>帖子或评论信息：</p>
 * <ul>
 * <li>postId: 帖子或评论ID</li>
 * <li>parentPostId: 帖子或评论的父亲ID</li>
 * <li>forumId: 论坛ID</li>
 * <li>categoryId: 类型ID</li>
 * <li>visibleFlag: 可见性范围类型，{@link com.everhomes.forum.PostVisibleFlag}</li>
 * <li>longitude: 帖子或评论内容涉及到的经度如活动</li>
 * <li>latitude: 帖子或评论内容涉及到的纬度如活动</li>
 * <li>category_id: 帖子或评论类型ID</li>
 * <li>subject: 帖子或评论标题</li>
 * <li>content_type: 帖子或评论内容类型，{@link com.everhomes.forum.PostContentType}</li>
 * <li>content: 帖子或评论内容</li>
 * <li>embeddedAppId: 内嵌对象对应的App ID，{@link com.everhomes.app.AppConstants}</li>
 * <li>embeddedId: 内嵌对象ID</li>
 * <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 * <li>isForwarded: 是否是转发帖的标记</li>
 * <li>childCount: 孩子数目，如帖子下的评论数目</li>
 * <li>forwardCount: 帖子或评论的转发数目</li>
 * <li>likeCount: 帖子或评论赞的数目</li>
 * <li>dislikeCount: 帖子或评论踩的数目</li>
 * <li>updateTime: 帖子或评论更新时间</li>
 * <li>createTime: 帖子或评论创建时间</li>
 * </ul>
 */
public class PostDTO {
	private Long postId;
	
	private Long parentPostId;
	
    private Long forumId;
    
    private Long categoryId;
    
    private Byte visibleFlag;
    
    private Double longitude;
    
    private Double latitude;
    
    private String subject;
    
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
    
    private String updateTime;
    
    private String createTime;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getVisibleFlag() {
		return visibleFlag;
	}

	public void setVisibleFlag(Byte visibleFlag) {
		this.visibleFlag = visibleFlag;
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
