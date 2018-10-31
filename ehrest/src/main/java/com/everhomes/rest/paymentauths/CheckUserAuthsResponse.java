package com.everhomes.rest.paymentauths;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>authsFlag: 是否被授权使用企业支付 1：是,0：否</li>
 * </ul>
 */
public class CheckUserAuthsResponse {
	private Byte authsFlag;

	public Byte getAuthsFlag() {
		return authsFlag;
	}

	public void setAuthsFlag(Byte authsFlag) {
		this.authsFlag = authsFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
