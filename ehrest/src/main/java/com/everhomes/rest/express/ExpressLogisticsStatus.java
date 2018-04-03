// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>IN_TRANSIT: 1，运送中</li>
 * <li>RECEIVED: 2，已签收</li>
 * </ul>
 */
public enum ExpressLogisticsStatus {
	IN_TRANSIT((byte)1), RECEIVED((byte)2);
	
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
