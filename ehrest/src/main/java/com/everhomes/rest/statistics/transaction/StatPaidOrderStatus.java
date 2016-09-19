package com.everhomes.rest.statistics.transaction;

public enum StatPaidOrderStatus {
	WAITING_PAID((byte)1), WAITING_DELIVER((byte)2), DELIVERED((byte)3), FINISH((byte)6), CLOSE((byte)7);
	
	private byte code;
	
	private StatPaidOrderStatus(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static StatPaidOrderStatus fromCode(Byte code){
		if(null == code){
			return null;
		}
		StatPaidOrderStatus[] values = StatPaidOrderStatus.values();
		for (StatPaidOrderStatus value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
