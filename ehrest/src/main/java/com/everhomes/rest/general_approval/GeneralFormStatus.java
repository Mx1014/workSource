package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>INVALID(0)无效状态,CONFIG(1)修改状态,RUNNING(2)运行状态</li>
 * </ul>
 * 
 * @author janson
 *
 */
public enum GeneralFormStatus {
	INVALID((byte)0),CONFIG((byte)1),RUNNING((byte)2);

	private byte code;

	private GeneralFormStatus(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static GeneralFormStatus fromCode(byte code) {
		for (GeneralFormStatus v : GeneralFormStatus.values()) {
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
