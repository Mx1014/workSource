package com.everhomes.rest.techpark.rental.admin;

/**
 * <ul>
 * 预约需要提交的信息
 * <li>attachmentType: 类型，参考
 * {@link com.everhomes.rest.techpark.rental.admin.AttachmentType}</li>
 * <li>mustOptions: 是否必选，1是0否</li>
 * </ul>
 */
public class Attachment {
	private Integer attachmentType;
	private Integer mustOptions;

	public Integer getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Integer attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Integer getMustOptions() {
		return mustOptions;
	}

	public void setMustOptions(Integer mustOptions) {
		this.mustOptions = mustOptions;
	}

}
