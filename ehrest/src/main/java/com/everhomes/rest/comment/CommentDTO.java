// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: 评论id</li>
 * <li>ownerToken: 实体标识</li>
 * <li>parentCommentId: 父评论的ID</li>
 * <li>replyToUserId: 被回复人的uid</li>
 * <li>creatorUid: 创建者id</li>
 * <li>creatorNickName: 创建者昵称</li>
 * <li>creatorAvatar: 创建者在圈内的头像URI</li>
 * <li>creatorAvatarUrl: 创建者在圈内的头像URL</li>
 * <li>contentType: 评论类型，参考{@link com.everhomes.rest.comment.ContentType}</li>
 * <li>content: 评论内容</li>
 * <li>createTime: 创建时间</li>
 * <li>attachments: 图片、语音、视频等附件信息，参考{@link com.everhomes.rest.comment.AttachmentDTO}</li>
 * </ul>
 */
public class CommentDTO {
	private Long id;
	private String ownerToken;
	private Long parentCommentId;
	private Long replyToUserId;
	private Long creatorUid;
	private String creatorNickName;
	private String creatorAvatar;
	private String creatorAvatarUrl;
	private String contentType;
	private String content;
	private Timestamp createTime;
	@ItemType(AttachmentDTO.class)
	private List<AttachmentDTO> attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerToken() {
		return ownerToken;
	}

	public void setOwnerToken(String ownerToken) {
		this.ownerToken = ownerToken;
	}

	public Long getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(Long parentCommentId) {
		this.parentCommentId = parentCommentId;
	}

	public Long getReplyToUserId() {
		return replyToUserId;
	}

	public void setReplyToUserId(Long replyToUserId) {
		this.replyToUserId = replyToUserId;
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

	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}