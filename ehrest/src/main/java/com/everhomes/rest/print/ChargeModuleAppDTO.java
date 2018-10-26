package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>chargeAppToken : 标识收费应用的token</li>
 * <li>chargeAppName : 收费应用的名称</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月20日
 */
public class ChargeModuleAppDTO {
	private Long chargeAppToken;
	private String chargeAppName;
	
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }

	public Long getChargeAppToken() {
		return chargeAppToken;
	}

	public void setChargeAppToken(Long chargeAppToken) {
		this.chargeAppToken = chargeAppToken;
	}

	public String getChargeAppName() {
		return chargeAppName;
	}

	public void setChargeAppName(String chargeAppName) {
		this.chargeAppName = chargeAppName;
	}

}
