package com.everhomes.rest.techpark.rental.admin;

/**
 * <ul>
 * <li>TEXT_REMARK: 文本备注</li>
 * <li>LICENSE_NUMBER: 车牌</li>
 * <li>SHOW_CONTENT: 显示内容</li>
 * <li>ATTACHMENT: 附件</li>
 * </ul>
 */
public enum AttachmentType {
	TEXT_REMARK("text_remark"), LICENSE_NUMBER("license_number"), SHOW_CONTENT("show_content"), ATTACHMENT(
			"attachment");
	private String code;

	private AttachmentType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static AttachmentType fromCode(String code) {
		if (code != null) {
			for (AttachmentType a : AttachmentType.values()) {
				if (code.equals(a.code)) {
					return a;
				}
			}
		}
		return null;
	}
}
