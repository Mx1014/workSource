package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>INACTIVE : 0</li>
 *	<li>ACTIVE : 1</li>
 * </ul>
 *
 */
public enum SearchTypesStatus {
	INACTIVE((byte)0), ACTIVE((byte)1);
	
	private byte code;
	
	private SearchTypesStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static SearchTypesStatus fromStatus(byte code) {
		for(SearchTypesStatus v : SearchTypesStatus.values()) {
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
