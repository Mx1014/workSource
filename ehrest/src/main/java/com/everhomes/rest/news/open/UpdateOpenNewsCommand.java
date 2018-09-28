package com.everhomes.rest.news.open;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>newsToken: 文章标识</li>
 * <li>title: 文章标题</li>
 * <li>contentAbstract: 摘要</li>
 * <li>coverUrl: 封面图url</li>
 * <li>content: 文章内容 html 或文本格式</li>
 * <li>author: 作者</li>
 * <li>sourceDesc: 文章来源</li>
 * <li>sourceUrl: 原文章链接</li>
 * <li>phone: 咨询电话</li>
 * <li>projectIds: 可见项目 id</li>
 * <li>newsTagIds: 文章标签 id (子标签)</li>
 * <li>status: 状态 2-发布状态 1-草稿状态</li>
 * </ul>
 */
public class UpdateOpenNewsCommand {

	@NotNull
	private String newsToken;
	
	@NotNull
	private String title;
	private String contentAbstract;
	private String coverUrl;
	
	@NotNull
	private String content;
	private String author;
	private String sourceDesc;
	private String sourceUrl;
	private String phone;
	
	@NotNull
	private List<Long> projectIds;
	private List<Long> newsTagIds;
	
	@NotNull
	private Byte status;
	
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Long> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(List<Long> projectIds) {
		this.projectIds = projectIds;
	}

	public List<Long> getNewsTagIds() {
		return newsTagIds;
	}

	public void setNewsTagIds(List<Long> newsTagIds) {
		this.newsTagIds = newsTagIds;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}
}
