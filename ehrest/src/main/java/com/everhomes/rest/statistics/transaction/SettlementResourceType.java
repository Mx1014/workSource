package com.everhomes.rest.statistics.transaction;

public enum SettlementResourceType {
	SHOP("shop"), PARKING_RECHARGE("parking_recharge"), RENTAL_SITE("rental_site"), PMSY("pmsy"), PAYMENT_CARD("payment_card");
	
	private String code;
	
	private SettlementResourceType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static SettlementResourceType fromCode(String code){
		if(null == code){
			return null;
		}
		SettlementResourceType[] values = SettlementResourceType.values();
		for (SettlementResourceType value : values) {
			if(value.code.equals(code)){
				return value;
			}
		}
		return null;
	}
	
}
