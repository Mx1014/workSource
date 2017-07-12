package com.everhomes.rest.techpark.expansion;

/**
 *
 * INACTIVE(1):已删除
 * ACTIVE(2): 正常
 * */
public enum LeaseIssuerStatus {

	INACTIVE((byte)0), ACTIVE((byte)2) ;

	private byte code;

	private LeaseIssuerStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static LeaseIssuerStatus fromType(byte code) {
		for(LeaseIssuerStatus v : LeaseIssuerStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
