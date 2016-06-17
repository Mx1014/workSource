package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属ID</li>
 * <li>newsToken: 新闻标识</li>
 * <li>topFlag: 现在的置顶状态，0未置顶，1已置顶，{@link com.everhomes.rest.news.NewsTopFlag}</li>
 * </ul>
 */
public class SetNewsTopFlagCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private String newsToken;
	@NotNull
	private Byte topFlag;

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

	public Byte getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(Byte topFlag) {
		this.topFlag = topFlag;
	}

}