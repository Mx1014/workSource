// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>WAITING_FOR_SENDING: 1，待收寄</li>
 * <li>IN_TRANSIT: 2，运送中</li>
 * <li>RECEIVED: 3，已签收</li>
 * <li>CANCELED: 4，已取消</li>
 * </ul>
 */
public enum ExpressLogisticsStatus {
	WAITING_FOR_SENDING((byte)1), IN_TRANSIT((byte)2), RECEIVED((byte)3), CANCELED((byte)4);
	
	private byte code;
	
	private ExpressLogisticsStatus(byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static ExpressLogisticsStatus fromCode(Byte code) {
		if (code != null) {
			for (ExpressLogisticsStatus expressOrderStatus : ExpressLogisticsStatus.values()) {
				if (expressOrderStatus.getCode().byteValue() == code.byteValue()) {
					return expressOrderStatus;
				}
			}
		}
		return null;
	} 
}
