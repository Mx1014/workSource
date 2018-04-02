package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>INVALID((byte) 0): INVALID</li>
 *     <li>VALID((byte) 2): VALID</li>
 * </ul>
 */
public enum OrganizationAppStatus {
	INVALID((byte) 0), VALID((byte) 2);
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
