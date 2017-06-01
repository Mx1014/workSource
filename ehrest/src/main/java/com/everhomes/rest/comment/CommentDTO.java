// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.NewsAttachmentDTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 评论id</li>
 * <li>entityToken: 实体标识</li>
 * <li>entityId: 实体ID</li>
 * <li>parentCommentId: 父评论的ID</li>
 * <li>creatorUid: 创建者id</li>
 * <li>creatorNickName: 创建者昵称</li>
 * <li>creatorAvatar: 创建者在圈内的头像URI</li>
 * <li>creatorAvatarUrl: 创建者在圈内的头像URL</li>
 * <li>contentType: 评论类型，参考{@link com.everhomes.rest.comment.ContentType}</li>
 * <li>content: 评论内容</li>
 * <li>createTime: 创建时间</li>
 * <li>attachments: 图片、语音、视频等附件信息，参考{@link com.everhomes.rest.comment.AttachmentDescriptor}</li>
 * </ul>
 */
public class CommentDTO {
	private Long id;
	private String entityToken;
	private Long entityId;
	private Long parentCommentId;
	private Long creatorUid;
	private String creatorNickName;
	private String creatorAvatar;
	private String creatorAvatarUrl;
	private String contentType;
	private String content;
	private Timestamp createTime;
	@ItemType(NewsAttachmentDTO.class)
	private List<NewsAttachmentDTO> attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityToken() {
		return entityToken;
	}

	public void setEntityToken(String entityToken) {
		this.entityToken = entityToken;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(Long parentCommentId) {
		this.parentCommentId = parentCommentId;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}