// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>WAITING_FOR_PAY: 1，待支持</li>
 * <li>PAID: 2，已支付</li>
 * <li>PRINTED: 3，已出单</li>
 * <li>CANCELLED: 4，已取消</li>
 * <li>FINISHED: 5，已完成</li>
 * </ul>
 */
public enum ExpressOrderStatus {
	WAITING_FOR_PAY((byte)1, "待支付"), PAID((byte)2,"已支付"), PRINTED((byte)3,"已出单"), CANCELLED((byte)4,"已取消"),FINISHED((byte)5,"已完成");
	
	private byte code;
	private String description;
	
	private ExpressOrderStatus(byte code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
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
