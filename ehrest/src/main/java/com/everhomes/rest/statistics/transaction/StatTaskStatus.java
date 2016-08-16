package com.everhomes.rest.statistics.transaction;

public enum StatTaskStatus {
	SYNC_SHOP_ORDER((byte)10), 
	SYNC_PMSY_ORDER((byte)20), 
	SYNC_PARKING_RECHARGE_ORDER((byte)30),
	SYNC_RENTAL_SITE_ORDER((byte)40),
	SYNC_PAYMENT_CARD_ORDER((byte)50),
	SYNC_PAID_PLATFORM_TRANSACTION((byte)60),
	SYNC_PAYMENT_CARD_TRANSACTION((byte)70),
	SYNC_PAID_PLATFORM_REFUND((byte)80),
	SYNC_PAYMENT_REFUND((byte)90),
	GENERATE_SETTLEMENT_DETAIL((byte)100),
	GENERATE_SETTLEMENT_RESULT((byte)110),
	FINISH((byte)120);
	
	private byte code;
	
	private StatTaskStatus(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static StatTaskStatus fromCode(Byte code){
		if(null == code){
			return null;
		}
		StatTaskStatus[] values = StatTaskStatus.values();
		for (StatTaskStatus value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
