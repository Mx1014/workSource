package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>phone: phone</li>
 *     <li>verificationCode: verificationCode</li>
 * </ul>
 */
public class BindPhoneCommand {

	private String phone;

	private String verificationCode;

	public BindPhoneCommand() {
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
