// @formatter:off

package com.everhomes.rest.news;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属ID</li>
 * <li>categoryId: 新闻类型ID</li>
 * <li>title: 标题</li>
 * <li>contentAbstract: 摘要</li>
 * <li>coverUri: 封面</li>
 * <li>content: 正文</li>
 * <li>author: 作者</li>
 * <li>publishTime: 发布时间</li>
 * <li>sourceDesc: 来源</li>
 * <li>sourceUrl: 原文链接</li>
 * <li>communityIds: 可见范围</li>
 * </ul>
 */
public class CreateNewsCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	private Long categoryId;
	@NotNull
	private String title;
	private String contentAbstract;
	private String coverUri;
	@NotNull
	private String content;
	private String author;
	private Long publishTime;
	private String sourceDesc;
	private String sourceUrl;
	@ItemType(Long.class)
	private List<Long> communityIds;
	private String visibleType;

	public String getVisibleType() {
		return visibleType;
	}

	public void setVisibleType(String visibleType) {
		this.visibleType = visibleType;
	}

	public List<Long> getCommunityIds() {
		return communityIds;
	}

	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public Long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}