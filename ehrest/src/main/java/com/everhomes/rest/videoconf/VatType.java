package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 0-一般纳税人 1-非一般纳税人
 *
 */
public enum VatType {

	GENERAL_TAXPAYER((byte)0), NON_GENERAL_TAXPAYER((byte)1);
	
	private byte code;
	
	private VatType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static VatType fromType(byte code) {
		for(VatType v : VatType.values()) {
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
