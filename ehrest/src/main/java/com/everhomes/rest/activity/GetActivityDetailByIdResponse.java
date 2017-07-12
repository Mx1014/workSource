// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>contentType: 内容类型，参考{@link com.everhomes.rest.forum.PostContentType}</li>
 * <li>content: 活动内容</li>
 * <li>attachments: 附件</li>
 * </ul>
 */
public class GetActivityDetailByIdResponse {
	private String contentType;
	private String content;
	@ItemType(AttachmentDTO.class)
	private List<AttachmentDTO> attachments;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public GetActivityDetailByIdResponse(String contentType, String content, List<AttachmentDTO> attachments) {
		super();
		this.contentType = contentType;
		this.content = content;
		this.attachments = attachments;
	}

	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
