package com.everhomes.rest.techpark.expansion;

/**
 *
 * INACTIVE(1):已删除
 * ACTIVE(2): 正常
 * */
public enum LeasePromotionDeleteFlag {

	NOTSUPPROT((byte)0), SUPPROT((byte)1) ;

	private byte code;

	private LeasePromotionDeleteFlag(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static LeasePromotionDeleteFlag fromType(byte code) {
		for(LeasePromotionDeleteFlag v : LeasePromotionDeleteFlag.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
