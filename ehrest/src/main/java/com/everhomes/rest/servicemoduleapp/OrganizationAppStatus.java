package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>DELETE((byte) 0): 删除的、无效的</li>
 *     <li>DISABLE((byte) 1): 禁用的</li>
 *     <li>ENABLE((byte) 2): 启用的</li>
 * </ul>
 */
public enum OrganizationAppStatus {
	DELETE((byte) 0), DISABLE((byte) 1), ENABLE((byte) 2);
	private Byte code;

	private OrganizationAppStatus(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public static OrganizationAppStatus fromCode(Byte code) {
		if (code != null) {
			for (OrganizationAppStatus a : OrganizationAppStatus.values()) {
				if (code.byteValue() == a.code.byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
