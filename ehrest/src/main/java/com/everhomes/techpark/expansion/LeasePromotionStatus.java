package com.everhomes.techpark.expansion;

/**
 * APPLY(1):招租中
 * EXPANSION(2): 已出租
 * RENEW(3):下线
 * */
public enum LeasePromotionStatus {
	
	RENTING((byte)1), RENTAL((byte)2), OFFLINE((byte)3);
	
	private byte code;
	
	private LeasePromotionStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static LeasePromotionStatus fromType(byte code) {
		for(LeasePromotionStatus v : LeasePromotionStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
