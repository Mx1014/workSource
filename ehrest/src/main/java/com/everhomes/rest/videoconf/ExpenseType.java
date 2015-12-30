package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * 0-电话/数据会议费
 *
 */
public enum ExpenseType {

	CONF((byte)0);
	
	private byte code;
	
	private ExpenseType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ExpenseType fromType(byte code) {
		for(ExpenseType v : ExpenseType.values()) {
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
