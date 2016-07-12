// @formatter:off

package com.everhomes.rest.news;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属ID</li>
 * <li>newsToken: 新闻标识</li>
 * <li>contentType: 评论类型，参考{@link com.everhomes.rest.news.NewsCommentContentType}</li>
 * <li>content: 评论内容</li>
 * <li>attachments: 图片、语音、视频等附件信息，参考{@link com.everhomes.rest.news.AttachmentDescriptor}</li>
 * </ul>
 */
public class AddNewsCommentCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private String newsToken;
	private String contentType;
	private String content;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachments;

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

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
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