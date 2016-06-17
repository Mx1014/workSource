package com.everhomes.rest.news;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 返回值
 * <li>postId: 帖子id</li>
 * <li>title: 标题</li>
 * <li>releaseTime: 发布时间</li>
 * <li>author: 作者</li>
 * <li>sourceDesc: 来源</li>
 * <li>sourceUrl: 原文链接</li>
 * <li>contentUrl: 正文链接</li>
 * <li>content: 正文</li>
 * <li>likeCount: 赞数量</li>
 * <li>childCount: 评论数量</li>
 * <li>viewCount: 查看数量</li>
 * </ul>
 */
public class NewsDTO {
	private Long postId;
	private String title;
	private Timestamp releaseTime;
	private String author;
	private String sourceDesc;
	private String sourceUrl;
	private String contentUrl;
	private String content;
	private Long likeCount;
	private Long childCount;
	private Long viewCount;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Timestamp releaseTime) {
		this.releaseTime = releaseTime;
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

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}