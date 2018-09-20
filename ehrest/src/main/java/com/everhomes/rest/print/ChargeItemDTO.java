package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>chargeItemToken : 收费项标识</li>
 * <li>chargeItemName : 收费项名称</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月20日
 */
public class ChargeItemDTO {
	private Long chargeItemToken;
	private String chargeItemName;
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
	public Long getChargeItemToken() {
		return chargeItemToken;
	}
	public void setChargeItemToken(Long chargeItemToken) {
		this.chargeItemToken = chargeItemToken;
	}
	public String getChargeItemName() {
		return chargeItemName;
	}
	public void setChargeItemName(String chargeItemName) {
		this.chargeItemName = chargeItemName;
	}
}
