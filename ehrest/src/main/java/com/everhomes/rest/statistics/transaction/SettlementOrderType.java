package com.everhomes.rest.statistics.transaction;

public enum SettlementOrderType {
	TRANSACTION("transaction"), REFUND("refund");
	
	private String code;
	
	private SettlementOrderType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static SettlementOrderType fromCode(String code){
		if(null == code){
			return null;
		}
		SettlementOrderType[] values = SettlementOrderType.values();
		for (SettlementOrderType value : values) {
			if(value.code.equals(code)){
				return value;
			}
		}
		return null;
	}
	
}
