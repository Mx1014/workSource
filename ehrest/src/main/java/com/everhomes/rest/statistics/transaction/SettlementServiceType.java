package com.everhomes.rest.statistics.transaction;

public enum SettlementServiceType {
	ZUOLIN_SHOP("zuolin_shop"), OTHER_SHOP("other_shop"), THIRD_SERVICE("third_service"), COMMUNITY_SERVICE("community_service");
	
	private String code;
	
	private SettlementServiceType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static SettlementServiceType fromCode(String code){
		if(null == code){
			return null;
		}
		SettlementServiceType[] values = SettlementServiceType.values();
		for (SettlementServiceType value : values) {
			if(value.code.equals(code)){
				return value;
			}
		}
		return null;
	}
	
}
