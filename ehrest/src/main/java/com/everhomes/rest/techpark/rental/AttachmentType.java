package com.everhomes.rest.techpark.rental;

/**
 * <ul>
 * <li>TEXT_REMARK: 文本备注</li>
 * <li>LICENSE_NUMBER: 车牌</li>
 * <li>SHOW_CONTENT: 显示内容</li>
 * <li>ATTACHMENT: 附件</li>
 * </ul>
 */
public enum AttachmentType {
	TEXT_REMARK((byte)0), LICENSE_NUMBER((byte)1), SHOW_CONTENT((byte)2), ATTACHMENT((byte)3);
	private Byte code;

	private AttachmentType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	public static AttachmentType fromCode(Byte code) {
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
