package com.everhomes.rest.ui.user;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>id: id</li>
 * <li>forumId: 论坛id</li>
 * <li>searchTypeId: 内容类型id</li>
 * <li>searchTypeName: 内容类型名称</li>
 * <li>contentType: 内容类型</li>
 * <li>subject: 主题</li>
 * <li>content: 内容</li>
 * <li>postUrl: 图片url（活动封面或帖子的第一张图）</li>
 * <li>footnoteJson: 脚注json(每种类型的不同返回字段)</li>
 *</ul>
 */
public class ContentBriefDTO {
	
	private Long id;
	
	private Long forumId;
	
	private Long searchTypeId;
	
	private String searchTypeName;
	
	private String contentType;
	
	private String subject;
	
	private String content;
	
	private String postUrl;
	
	private String footnoteJson;
	
	private String newsToken;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public Long getSearchTypeId() {
		return searchTypeId;
	}

	public void setSearchTypeId(Long searchTypeId) {
		this.searchTypeId = searchTypeId;
	}

	public String getSearchTypeName() {
		return searchTypeName;
	}

	public void setSearchTypeName(String searchTypeName) {
		this.searchTypeName = searchTypeName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public String getFootnoteJson() {
		return footnoteJson;
	}

	public void setFootnoteJson(String footnoteJson) {
		this.footnoteJson = footnoteJson;
	}

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
