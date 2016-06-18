package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>INACTIVE : 0</li>
 *	<li>ACTIVE : 1</li>
 * </ul>
 *
 */
public enum HotTagStatus {

	INACTIVE((byte)0), ACTIVE((byte)1);
	
	private byte code;
	
	private HotTagStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static HotTagStatus fromStatus(byte code) {
		for(HotTagStatus v : HotTagStatus.values()) {
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
