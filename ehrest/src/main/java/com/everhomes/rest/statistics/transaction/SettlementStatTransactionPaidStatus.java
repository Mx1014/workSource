package com.everhomes.rest.statistics.transaction;

public enum SettlementStatTransactionPaidStatus {
	WAITING((byte)0), PAID((byte)1), FAIL((byte)2);
	
	private byte code;
	
	private SettlementStatTransactionPaidStatus(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static SettlementStatTransactionPaidStatus fromCode(Byte code){
		if(null == code){
			return null;
		}
		SettlementStatTransactionPaidStatus[] values = SettlementStatTransactionPaidStatus.values();
		for (SettlementStatTransactionPaidStatus value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
