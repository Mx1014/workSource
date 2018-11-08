package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>缴费账单状态, 0-未缴, 1-已缴</li>
 * </ul>
 * Created by djm on 2018/10/18.
 */
public enum AssetBillStatusType {

	UNPAID((byte) 0, "未缴"), PAID((byte) 1, "已缴");

	private byte code;
	private String desc;

	private AssetBillStatusType(byte code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public byte getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public static AssetBillStatusType fromCode(Byte code) {
		if (code != null) {
			for (AssetBillStatusType status : AssetBillStatusType.values()) {
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
