package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 创建一条新闻
 * <li>sceneToken: 场景标识</li>
 * <li>title: 标题</li>
 * <li>contentAbstract: 摘要</li>
 * <li>coverUri: 封面</li>
 * <li>content: 正文</li>
 * <li>author: 作者</li>
 * <li>original: 来源</li>
 * <li>originalLink: 原文链接</li>
 * </ul>
 */
public class CreateNewsCommand {
	private String sceneToken;
	@NotNull
	private String title;
	private String contentAbstract;
	private String coverUri;
	@NotNull
	private String content;
	private String author;
	private String original;
	private String originalLink;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
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

	public String getCoverUri() {
		return coverUri;
	}

	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
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

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getOriginalLink() {
		return originalLink;
	}

	public void setOriginalLink(String originalLink) {
		this.originalLink = originalLink;
	}

}