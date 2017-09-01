package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>phone: phone</li>
 *     <li>verificationCode: verificationCode</li>
 *     <li>bindPhoneType: 绑定方式 0-手机绑定到微信用户，1-微信用户绑定到已有手机用户  参考{@link BindPhoneType}</li>
 *     <li>password: password</li>
 *     <li>oldPhone: oldPhone</li>
 * </ul>
 */
public class BindPhoneCommand {

	private String phone;

	private String verificationCode;

	private Byte bindPhoneType;

	private String password;

	private String oldPhone;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public Byte getBindPhoneType() {
		return bindPhoneType;
	}

	public void setBindPhoneType(Byte bindPhoneType) {
		this.bindPhoneType = bindPhoneType;
	}

	public String getOldPhone() {
		return oldPhone;
	}

	public void setOldPhone(String oldPhone) {
		this.oldPhone = oldPhone;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
