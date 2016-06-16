package com.everhomes.rest.news;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 查询新闻详情
 * <li>postId: 帖子id</li>
 * <li>title: 标题</li>
 * <li>releaseTime: 发布时间</li>
 * <li>author: 作者</li>
 * <li>original: 来源</li>
 * <li>contentLink: 正文链接</li>
 * <li>originalLink: 原文链接</li>
 * <li>likeCount: 赞数量</li>
 * <li>childCount: 评论数量</li>
 * <li>viewCount: 查看数量</li>
 * </ul>
 */
public class QueryNewsDetailResponse {
	private Long postId;
	private String title;
	private Timestamp releaseTime;
	private String author;
	private String original;
	private String contentLink;
	private String originalLink;
	private Long likeCount;
	private Long childCount;
	private Long viewCount;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getContentLink() {
		return contentLink;
	}

	public void setContentLink(String contentLink) {
		this.contentLink = contentLink;
	}

	public String getOriginalLink() {
		return originalLink;
	}

	public void setOriginalLink(String originalLink) {
		this.originalLink = originalLink;
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

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

}