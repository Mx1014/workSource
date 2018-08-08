package com.everhomes.rest.news.open;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>newsToken: 文章标识</li>
 * <li>title: 标题</li>
 * <li>contentAbstract: 摘要</li>
 * <li>coverUrl: 封面图 url</li>
 * <li>author: 作者</li>
 * <li>publishTime: 发布时间戳</li>
 * <li>sourceDesc: 文章来源</li>
 * <li>sourceUrl: 原文链接</li>
 * <li>phone: 咨询电话</li>
 * <li>status: 新闻状态,2-发布状态 1-草稿状态 参考{@link com.everhomes.rest.news.NewsStatus}</li>
 * <li>ownerId: 所属项目 id</li>
 * <li>categoryId: 标识所属快讯应用</li>
 * </ul>
 */
public class OpenBriefNewsDTO {

	private String newsToken;
	private String title;
	private String contentAbstract;
	private String coverUrl;
	private String author;
	private Long publishTime;
	private String sourceDesc;
	private String sourceUrl;
	private String phone;
	private Byte status;
	private Long ownerId;
	private Long categoryId;
	

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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
	public String getContentAbstract() {
		return contentAbstract;
	}
	public void setContentAbstract(String contentAbstract) {
		this.contentAbstract = contentAbstract;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
