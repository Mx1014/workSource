package com.everhomes.rest.techpark.expansion;

/**
 *
 * INACTIVE(0):无效
 * ACTIVE(2): 正常
 * */
public enum LeaseBulidingStatus {

	INACTIVE((byte)0), ACTIVE((byte)2) ;

	private byte code;

	private LeaseBulidingStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static LeaseBulidingStatus fromType(byte code) {
		for(LeaseBulidingStatus v : LeaseBulidingStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
