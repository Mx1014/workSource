package com.everhomes.rest.statistics.transaction;

public enum StatRefundOrderStatus {
	UN_APPLY((byte)1), WAITING((byte)2), REJECT((byte)3), REFUNDING((byte)4), SUCCESS((byte)5) ,CLOSE((byte)6);
	
	private byte code;
	
	private StatRefundOrderStatus(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static StatRefundOrderStatus fromCode(Byte code){
		if(null == code){
			return null;
		}
		StatRefundOrderStatus[] values = StatRefundOrderStatus.values();
		for (StatRefundOrderStatus value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
