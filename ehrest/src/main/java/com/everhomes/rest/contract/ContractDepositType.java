package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>押金状态, 1-未缴, 2-已缴</li>
 * </ul>
 * Created by djm on 2018/9/29.
 */
public enum ContractDepositType {

	UNPAID((byte) 1, "未缴"), PAID((byte) 2, "已缴");

	private byte code;
	private String desc;

	private ContractDepositType(byte code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public byte getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public static ContractDepositType fromCode(Byte code) {
		if (code != null) {
			for (ContractDepositType status : ContractDepositType.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
