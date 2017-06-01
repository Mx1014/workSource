// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>ownerType: 评论来源，如快讯、论坛等, 参考{@link com.everhomes.rest.comment.OwnerType}</li>
 * <li>ownerToken: 实体标识</li>
 * <li>parentCommentId: 父评论的ID</li>
 * <li>content: 帖子内容</li>
 * <li>contentType: 评论类型，参考{@link com.everhomes.rest.comment.ContentType}</li>
 * <li>attachments: 图片、语音、视频等附件信息，参考{@link com.everhomes.rest.comment.AttachmentDescriptor}</li>
 * </ul>
 */
public class AddCommentCommand {
	@NotNull
	private Byte ownerType;
	private String ownerToken;
	private Long parentCommentId;
	private String content;
	private String contentType;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachments;

	public Byte getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}