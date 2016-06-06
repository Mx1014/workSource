package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>verifyCode: 验证码</li>
 * </ul>
 */
public class SendCardVerifyCodeDTO {
	private String verifyCode;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
}
