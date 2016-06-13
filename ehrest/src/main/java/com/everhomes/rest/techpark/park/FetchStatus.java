package com.everhomes.rest.techpark.park;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>NO: 0</li>
 *  <li>YES: 1</li>
 * </ul>
 *
 */
public enum FetchStatus {

	NO((byte)0), YES((byte)1);
	
	private byte code;
	
	private FetchStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static FetchStatus fromStatus(byte code) {
		for(FetchStatus v : FetchStatus.values()) {
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
