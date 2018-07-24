//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/16.
 */

public enum  AdjustType {
   INCREASE_QUANTITY((byte)1), DECREASE_QUANTITY((byte)2), INCREASE_PROPORTION((byte)3), DECREASE_PROPORTION((byte)4);
   private byte code;

	AdjustType(byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return this.code;
	}

	public static AdjustType fromCode(Byte code) {
		if (code != null) {
			for (AdjustType status: AdjustType.values()) {
				if (status.getCode().byteValue() == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}
}
