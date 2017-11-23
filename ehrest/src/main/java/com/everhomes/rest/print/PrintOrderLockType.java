// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>UNLOCKED(0): 订单可添加状态</li>
 * <li>LOCKED(1) : 订单进入支付状态,不可以在此订单上添加</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintOrderLockType {
	UNLOCKED((byte)0),LOCKED((byte)1);
	
	private byte code;

	private PrintOrderLockType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static PrintOrderLockType fromCode(Byte code) {
		if(null == code)
			return null;
		for (PrintOrderLockType t : PrintOrderLockType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}

