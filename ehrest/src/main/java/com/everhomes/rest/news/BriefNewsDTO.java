// @formatter:off

package com.everhomes.rest.news;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 返回值
 * <li>newsToken: 新闻标识</li>
 * <li>title: 标题</li>
 * <li>publishTime: 发布时间</li>
 * <li>author: 作者</li>
 * <li>sourceDesc: 来源</li>
 * <li>coverUri: 封面</li>
 * <li>contentAbstract: 摘要</li>
 * <li>likeCount: 赞数量</li>
 * <li>childCount: 评论数量</li>
 * <li>topFlag: 置顶标记，0未置顶，1已置顶，参考{@link com.everhomes.rest.news.NewsTopFlag}</li>
 * <li>likeFlag: 点赞状态，0未点赞，1不喜欢，2已点赞，参考{@link com.everhomes.rest.user.UserLikeType}</li>
 * <li>newsUrl: 新闻链接(供分享)</li>
 * <li>phone: 联系电话</li>
 * <li>categoryId: 新闻类型ID</li>
 * <li>highlightFields: 新闻类型ID</li>
 * <li>commentFlag: 新闻是否可以评论，1：可以 0：禁止 {@link NewsNormalFlag}</li>
 * </ul>
 */
public class BriefNewsDTO {
	private String newsToken;
	private String title;
	private Timestamp publishTime;
	private String author;
	private String sourceDesc;
	private String coverUri;
	private String contentAbstract;
	private Long likeCount;
	private Long childCount;
	private Byte topFlag;
	private Byte likeFlag;
	private String newsUrl;
	private Long phone;
	private Long categoryId;
	private Byte commentFlag;
	private String visibleType;

	private String highlightFields;

	@ItemType(ProjectDTO.class)
	private List<ProjectDTO> projectDTOS;

	public List<ProjectDTO> getProjectDTOS() {
		return projectDTOS;
	}

	public void setProjectDTOS(List<ProjectDTO> projectDTOS) {
		this.projectDTOS = projectDTOS;
	}

	public String getVisibleType() {
		return visibleType;
	}

	public void setVisibleType(String visibleType) {
		this.visibleType = visibleType;
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

	public Byte getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(Byte topFlag) {
		this.topFlag = topFlag;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getHighlightFields() {
		return highlightFields;
	}

	public void setHighlightFields(String highlightFields) {
		this.highlightFields = highlightFields;
	}
}