// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>WAITING_FOR_PAY: 1，待支持</li>
 * <li>PAID: 2，已支付</li>
 * <li>PRINTED: 3，已出单</li>
 * <li>CANCELLED: 4，已取消</li>
 * </ul>
 */
public enum ExpressOrderStatus {
	WAITING_FOR_PAY((byte)1), PAID((byte)2), PRINTED((byte)3), CANCELLED((byte)4);
	
	private byte code;
	
	private ExpressOrderStatus(byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static ExpressOrderStatus fromCode(Byte code) {
		if (code != null) {
			for (ExpressOrderStatus expressOrderStatus : ExpressOrderStatus.values()) {
				if (expressOrderStatus.getCode().byteValue() == code.byteValue()) {
					return expressOrderStatus;
				}
			}
		}
		return null;
	} 
}
