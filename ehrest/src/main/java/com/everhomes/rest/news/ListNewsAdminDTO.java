package com.everhomes.rest.news;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 后台查询新闻列表
 * <li>id: 新闻id</li>
 * <li>title: 标题</li>
 * <li>releaseTime: 发布时间</li>
 * <li>author: 作者</li>
 * <li>original: 来源</li>
 * <li>coverUri: 封面</li>
 * <li>contentAbstract: 摘要</li>
 * <li>likeCount: 赞数量</li>
 * <li>childCount: 评论数量</li>
 * <li>flag: 置顶标记，0未置顶，1置顶</li>
 * </ul>
 */
public class ListNewsAdminDTO {
	private Long id;
	private String title;
	private Timestamp releaseTime;
	private String author;
	private String original;
	private String coverUri;
	private String contentAbstract;
	private Long likeCount;
	private Long childCount;
	private Byte flag;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getFlag() {
		return flag;
	}

	public void setFlag(Byte flag) {
		this.flag = flag;
	}

}