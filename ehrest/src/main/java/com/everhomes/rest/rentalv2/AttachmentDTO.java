package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 预约需要提交的信息
 * <li>attachmentType: 类型，参考
 * {@link com.everhomes.rest.rentalv2.admin.AttachmentType}</li>
 * <li>content: 内容-文本</li>
 * </ul>
 */
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
