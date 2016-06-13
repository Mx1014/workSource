package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 0-公司 1-个人
 *
 */
public enum TaxpayerType {

	COMPANY((byte)0), INDIVIDUAL((byte)1);
	
	private byte code;
	
	private TaxpayerType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static TaxpayerType fromType(byte code) {
		for(TaxpayerType v : TaxpayerType.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
