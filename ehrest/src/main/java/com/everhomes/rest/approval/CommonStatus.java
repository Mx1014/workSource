// @formatter:off
package com.everhomes.rest.approval;

/**
 * 
 * <ul>通用状态：
 * <li>INACTIVE: 0，已失效</li>
 * <li>WAITING_FOR_CONFIRMATION: 1，待确认</li>
 * <li>ACTIVE: 2，有效</li>
 * </ul>
 */
public enum CommonStatus {
	INACTIVE((byte)0), WAITING_FOR_CONFIRMATION((byte)1), ACTIVE((byte)2);
	
	private byte code;
	
	private CommonStatus(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public static CommonStatus fromCode(Byte code){
		if (code != null) {
			for (CommonStatus status : CommonStatus.values()) {
				if (code.byteValue() == status.getCode()) {
					return status;
				}
			}
		}
		
		return null;
	}
}
