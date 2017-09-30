package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>WECHATTOPHONE((byte) 0): WECHATTOPHONE</li>
 *     <li>PHONETOWECHAT((byte) 1): PHONETOWECHAT</li>
 *     <li>ALREADYBIND((byte) 2): ALREADYBIND</li>
 * </ul>
 */
public enum BindPhoneType {
	WECHATTOPHONE((byte) 0), PHONETOWECHAT((byte) 1), ALREADYBIND((byte) 2);

	private byte code;

	private BindPhoneType(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static BindPhoneType fromStatus(byte code) {
		for (BindPhoneType v : BindPhoneType.values()) {
			if (v.getCode() == code)
				return v;
		}
		return null;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
