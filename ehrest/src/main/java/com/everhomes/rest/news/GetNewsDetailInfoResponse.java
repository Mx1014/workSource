// @formatter:off

package com.everhomes.rest.news;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 返回值
 * <li>newsToken: 新闻标识</li>
 * <li>title: 标题</li>
 * <li>publishTime: 发布时间</li>
 * <li>author: 作者</li>
 * <li>sourceDesc: 来源</li>
 * <li>sourceUrl: 原文链接</li>
 * <li>contentUrl: 正文链接</li>
 * <li>content: 正文</li>
 * <li>likeCount: 赞数量</li>
 * <li>childCount: 评论数量</li>
 * <li>viewCount: 查看数量</li>
 * <li>likeFlag: 点赞状态，0未点赞，1不喜欢，2已点赞，参考{@link com.everhomes.rest.user.UserLikeType}</li>
 * <li>newsUrl: 新闻链接-供分享</li>
 * <li>coverUri: 封面</li>
 * </ul>
 */
public class GetNewsDetailInfoResponse {
	private String newsToken;
	private String title;
	private Timestamp publishTime;
	private String author;
	private String sourceDesc;
	private String sourceUrl;
	private String contentUrl;
	private String content;
	private Long likeCount;
	private Long childCount;
	private Long viewCount;
	private Byte likeFlag;
	private String newsUrl;
	private String coverUri;
	private Byte commentFlag;
	private Integer namespaceId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getCommentFlag() {
		return commentFlag;
	}

	public void setCommentFlag(Byte commentFlag) {
		this.commentFlag = commentFlag;
	}

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSourceDesc() {
		return sourceDesc;
	}

	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}

	public Long getChildCount() {
		return childCount;
	}

	public void setChildCount(Long childCount) {
		this.childCount = childCount;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Byte getLikeFlag() {
		return likeFlag;
	}

	public void setLikeFlag(Byte likeFlag) {
		this.likeFlag = likeFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public String getCoverUri() {
		return coverUri;
	}

	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}

}