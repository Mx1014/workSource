// @formatter:off

package com.everhomes.rest.comment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>sceneToken: 场景标识</li>
 * <li>sourceType: 评论来源，如快讯、论坛等, 参考{@link com.everhomes.rest.comment.SourceType}</li>
 * <li>entityToken: 实体标识</li>
 * <li>entityId: 实体ID</li>
 * <li>parentCommentId: 父评论的ID</li>
 * <li>content: 帖子内容</li>
 * <li>contentType: 评论类型，参考{@link com.everhomes.rest.comment.ContentType}</li>
 * <li>attachments: 图片、语音、视频等附件信息，参考{@link com.everhomes.rest.comment.AttachmentDescriptor}</li>
 * </ul>
 */
public class AddCommentCommand {
	private String sceneToken;
	@NotNull
	private Byte sourceType;
	private String entityToken;
	private Long entityId;
	private Long parentCommentId;
	private String content;
	private String contentType;
	private Long embeddedAppId;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachments;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(Byte sourceType) {
		this.sourceType = sourceType;
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

	public Long getEmbeddedAppId() {
		return embeddedAppId;
	}

	public void setEmbeddedAppId(Long embeddedAppId) {
		this.embeddedAppId = embeddedAppId;
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