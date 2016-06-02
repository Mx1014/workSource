package com.everhomes.rest.techpark.rental;

import com.everhomes.util.StringHelper;

public class AttachmentDTO {
	private Byte attachmentType;
	private String content;
	public Byte getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(Byte attachmentType) {
		this.attachmentType = attachmentType;
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
