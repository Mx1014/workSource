// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>content: 活动内容</li>
 * </ul>
 */
public class GetActivityDetailByIdResponse {
	private String content;
	private List<AttachmentDTO> attachments;

	public GetActivityDetailByIdResponse(String content, List<AttachmentDTO> attachments) {
		super();
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
