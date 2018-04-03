package com.everhomes.rest.openapi.techpark;

public enum CustomerLivingStatus {
//	0-非租赁（其他） 
//	1-未放租（其他）
//	2-待租（空闲）
//	3-保留（其他）
//	4- 新租（出租)
//	5-续租（出租） 
//	6-扩租（出租）
//	7-保留（其他）
	
	NOT_RENTAL((byte)0),
	NOT_RENTING((byte)1),
	WAITING_FOR_RENTING((byte)2),
	RESERVE((byte)3),
	NEW_RENTING((byte)4),
	CONTINUE_RENTING((byte)5),
	EXTEND_RENTING((byte)6),
	RETAIN((byte)7);
	
	private byte code;
	
	private CustomerLivingStatus(byte code) {
		this.code = code;
	}
	
	public byte getCode() {
		return this.code;
	}
	
	public static CustomerLivingStatus fromCode(Byte code) {
		if (code != null) {
			for (CustomerLivingStatus customerLivingStatus : CustomerLivingStatus.values()) {
				if (customerLivingStatus.getCode() == code.byteValue()) {
					return customerLivingStatus;
				}
			}
		}
		return null;
	}
}
