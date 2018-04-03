// @formatter:off
package com.everhomes.rest.print;

/**
 * 
 * <ul>
 * <li>UNPAID(1) : 未支付</li>
 * <li>PAID(2) : 已支付</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintOrderStatusType {
	// * <li>INACTIVE(0) : 没激活</li>
//	INACTIVE((byte)0),
	UNPAID((byte)1),PAID((byte)2);
	
	private byte code;

	private PrintOrderStatusType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static PrintOrderStatusType fromCode(Byte code) {
		if(code == null)
			return null;
		for (PrintOrderStatusType t : PrintOrderStatusType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
