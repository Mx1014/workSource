package com.everhomes.rest.statistics.transaction;

public enum SettlementStatOrderStatus {
	WAITING((byte)0), PAID((byte)1), FAIL((byte)2);
	
	private byte code;
	
	private SettlementStatOrderStatus(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static SettlementStatOrderStatus fromCode(Byte code){
		if(null == code){
			return null;
		}
		SettlementStatOrderStatus[] values = SettlementStatOrderStatus.values();
		for (SettlementStatOrderStatus value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
