package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>bindPhoneType: 绑定方式 0-微信用户绑定到已有手机用户，1-手机绑定到微信用户，2-微信用户已经和手机绑定过将更新手机  参考{@link BindPhoneType}</li>
 *     <li>oldPhone: 已绑定的手机，如果bindPhoneType=2则返回之前的手机</li>
 * </ul>
 */
public class VerificationCodeForBindPhoneResponse {

	private Byte bindPhoneType;

	private String oldPhone;

	public VerificationCodeForBindPhoneResponse() {
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
