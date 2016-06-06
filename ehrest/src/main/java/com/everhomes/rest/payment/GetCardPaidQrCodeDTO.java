package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>code: 卡支付信息</li>
 * </ul>
 */
public class GetCardPaidQrCodeDTO {
	private String code;

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
