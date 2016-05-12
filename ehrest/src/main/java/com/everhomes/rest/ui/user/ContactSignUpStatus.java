package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * ALL((byte)0): 通讯录中所有联系人
 * SIGNEDUP((byte)1): 通讯录中左邻用户
 *
 */
public enum ContactSignUpStatus {

	ALL((byte)0), SIGNEDUP((byte)1);
	
	private byte code;
	
	private ContactSignUpStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ContactSignUpStatus fromStatus(byte code) {
		for(ContactSignUpStatus v : ContactSignUpStatus.values()) {
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
