// @formatter:off
package com.everhomes.rest.print;

/**
 * 
 * <ul>
 * <li>UNPAID(1) : 未支付</li>
 * <li>PAID(2) : 已支付</li>
 * <li>WAIT_FOR_ENTERPRISE_PAY(3) : 已记账</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintOrderStatusType {
	UNPAID((byte)1),PAID((byte)2),WAIT_FOR_ENTERPRISE_PAY((byte)3);
	
	private Byte code;

	private PrintOrderStatusType(byte code){
		this.code = code;
	}

	public Byte getCode() {
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
