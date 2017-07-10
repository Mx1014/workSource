package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NORMAL : 0</li>
 *	<li>HOT : 1</li>
 * </ul>
 *
 */
public enum HotFlag {

	NORMAL((byte)0), HOT((byte)1);

	private byte code;

	private HotFlag(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static HotFlag fromStatus(byte code) {
		for(HotFlag v : HotFlag.values()) {
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
