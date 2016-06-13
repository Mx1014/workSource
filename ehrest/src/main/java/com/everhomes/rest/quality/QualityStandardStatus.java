package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>INACTIVE : 0</li>
 *	<li>WAITING : 1</li>
 *	<li>ACTIVE : 2</li>
 * </ul>
 *
 */
public enum QualityStandardStatus {
	
	INACTIVE((byte)0), WAITING((byte)1), ACTIVE((byte)2);
	
	private byte code;
	
	private QualityStandardStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityStandardStatus fromStatus(byte code) {
		for(QualityStandardStatus v : QualityStandardStatus.values()) {
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
