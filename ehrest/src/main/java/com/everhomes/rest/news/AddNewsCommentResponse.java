// @formatter:off

package com.everhomes.rest.news;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 返回值
 * <li>Id: 评论id</li>
 * <li>newsToken: 新闻标识</li>
 * <li>creatorUid: 创建者id</li>
 * <li>creatorNickName: 创建者昵称</li>
 * <li>creatorAvatar: 创建者在圈内的头像URI</li>
 * <li>creatorAvatarUrl: 创建者在圈内的头像URL</li>
 * <li>contentType: 内容类型，参考{@link com.everhomes.rest.news.NewsCommentContentType}</li>
 * <li>content: 评论内容</li>
 * <li>createTime: 创建时间</li>
 * <li>attachments: 附件信息，参考{@link com.everhomes.rest.news.NewsAttachmentDTO}</li>
 * <li>deleteFlag: 是否可删除，0不可删除，1可删除，参考{@link com.everhomes.rest.news.NewsCommentDeleteFlag}</li>
 * </ul>
 */
public class AddNewsCommentResponse {
	private Long Id;
	private String newsToken;
	private Long creatorUid;
	private String creatorNickName;
	private String creatorAvatar;
	private String creatorAvatarUrl;
	private String contentType;
	private String content;
	private Timestamp createTime;
	@ItemType(NewsAttachmentDTO.class)
	private List<NewsAttachmentDTO> attachments;
	private Byte deleteFlag;

	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public String getCreatorNickName() {
		return creatorNickName;
	}

	public void setCreatorNickName(String creatorNickName) {
		this.creatorNickName = creatorNickName;
	}

	public String getCreatorAvatar() {
		return creatorAvatar;
	}

	public void setCreatorAvatar(String creatorAvatar) {
		this.creatorAvatar = creatorAvatar;
	}

	public String getCreatorAvatarUrl() {
		return creatorAvatarUrl;
	}

	public void setCreatorAvatarUrl(String creatorAvatarUrl) {
		this.creatorAvatarUrl = creatorAvatarUrl;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<NewsAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<NewsAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}