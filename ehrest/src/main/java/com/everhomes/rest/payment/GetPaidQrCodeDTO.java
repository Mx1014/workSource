package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>token: 卡支付信息</li>
 * </ul>
 */
public class GetPaidQrCodeDTO {
	private String token;

	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}

}
