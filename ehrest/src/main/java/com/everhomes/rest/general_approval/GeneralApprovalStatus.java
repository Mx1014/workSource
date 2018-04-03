package com.everhomes.rest.general_approval;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>DELETED(0)删除状态,INVALID(1)无效状态,RUNNING(2)运行状态</li>
 * </ul>
 * 
 * @author janson
 *
 */
public enum GeneralApprovalStatus {
	DELETED((byte)0),INVALID((byte)1),RUNNING((byte)2);

	private byte code;

	private GeneralApprovalStatus(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static GeneralApprovalStatus fromCode(byte code) {
		for (GeneralApprovalStatus v : GeneralApprovalStatus.values()) {
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
