package com.everhomes.rest.techpark.expansion;

/**
 * <ul>
 * <li>PARK_INTRODUCE(1):项目介绍</li>
 * <li>LEASE_PROMOTION(2):房源招租</li>
 * </ul>
 * */
public enum LeasePromotionOrder {

	PARK_INTRODUCE((byte)1), LEASE_PROMOTION((byte)2);

	private byte code;

	private LeasePromotionOrder(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static LeasePromotionOrder fromType(byte code) {
		for(LeasePromotionOrder v : LeasePromotionOrder.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
