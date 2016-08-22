package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 预约需要提交的信息
 * <li>attachmentType: 类型，参考
 * {@link com.everhomes.rest.rentalv2.admin.AttachmentType}</li>
 * <li>mustOptions: 是否必选，1是0否</li>
 * </ul>
 */
public class AttachmentConfigDTO {
	private Byte attachmentType;
	private Byte mustOptions;

	public Byte getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Byte attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Byte getMustOptions() {
		return mustOptions;
	}

	public void setMustOptions(Byte mustOptions) {
		this.mustOptions = mustOptions;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
