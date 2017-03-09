// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>phone: 手机号</li>
 * </ul>
 */
public class VertifyPersonByPhoneCommand {
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
