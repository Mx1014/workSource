package com.everhomes.rest.news;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * APP端查询新闻列表
 * <li>id: 新闻id</li>
 * <li>title: 标题</li>
 * <li>releaseTime: 发布时间</li>
 * <li>coverUri: 封面</li>
 * <li>contentAbstract: 摘要</li>
 * </ul>
 */
public class ListNewsAppDTO {
	private Long id;
	private String title;
	private Timestamp releaseTime;
	private String coverUri;
	private String contentAbstract;

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

	public String getCoverUri() {
		return coverUri;
	}

	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}

	public String getContentAbstract() {
		return contentAbstract;
	}

	public void setContentAbstract(String contentAbstract) {
		this.contentAbstract = contentAbstract;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}