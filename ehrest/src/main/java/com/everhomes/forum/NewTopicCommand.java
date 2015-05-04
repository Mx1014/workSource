// @formatter:off
package com.everhomes.forum;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>categoryId: 类型ID</li>
 * <li>visibilityScope: 可见性范围类型，{@link com.everhomes.visibility.VisibilityScope}</li>
 * <li>visibilityScopeId: 可见性范围类型对应的ID</li>
 * <li>longitude: 帖子内容涉及到的经度如活动</li>
 * <li>latitude: 帖子内容涉及到的纬度如活动</li>
 * <li>category_id: 帖子类型ID</li>
 * <li>subject: 帖子标题</li>
 * <li>content_type: 帖子内容类型，{@link com.everhomes.forum.PostContentType}</li>
 * <li>content: 帖子内容</li>
 * <li>embeddedType: 内嵌对象类型，{@link com.everhomes.forum.PostEmbeddedType}</li>
 * <li>embeddedJson: 内嵌对象列表对应的json字符串</li>
 * <li>isForwarded: 是否是转发帖的标记</li>
 * </ul>
 */
public class NewTopicCommand {
    @NotNull
    private Long forumId;
    
    private Long categoryId;
    
    private Byte visibilityScope;
    
    private Long visibilityScopeId;
    
    private Double longitude;
    
    private Double latitude;
    
    @NotNull
    private String subject;
    
    @NotNull
    private String content;
    
    private String embeddedType;
    
    private Long embeddedId;
    
    // json encoded List<String> 
    private String embeddedJson;
    
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

	public String getEmbeddedType() {
		return embeddedType;
	}

	public void setEmbeddedType(String embeddedType) {
		this.embeddedType = embeddedType;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
