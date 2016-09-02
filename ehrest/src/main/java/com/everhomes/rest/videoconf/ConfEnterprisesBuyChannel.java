package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>OFFLINE: 0 线下</li>
 *  <li>ONLINE： 1 线上</li>
 *  <li>BOTH： 2 线下和线上</li>
 * </ul>
 *
 */
public enum ConfEnterprisesBuyChannel {

	OFFLINE((byte)0), ONLINE((byte)1), BOTH((byte)2);
	
	private byte code;
	
	private ConfEnterprisesBuyChannel(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ConfEnterprisesBuyChannel fromStatus(byte code) {
		for(ConfEnterprisesBuyChannel v : ConfEnterprisesBuyChannel.values()) {
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
