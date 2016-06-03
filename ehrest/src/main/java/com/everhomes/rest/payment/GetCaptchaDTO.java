package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>captcha: 验证码</li>
 * </ul>
 */
public class GetCaptchaDTO {
	private String captcha;

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
