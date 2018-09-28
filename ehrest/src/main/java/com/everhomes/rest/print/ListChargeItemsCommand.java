package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId : namespaceId</li>
 * <li>chargeAppToken : 收费应用标识</li>
 * <li>billGroupToken : 帐单组标识</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月20日
 */
public class ListChargeItemsCommand {
	private Integer namespaceId;
	private Long chargeAppToken;
	private Long billGroupToken;
	
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getChargeAppToken() {
		return chargeAppToken;
	}

	public void setChargeAppToken(Long chargeAppToken) {
		this.chargeAppToken = chargeAppToken;
	}

	public Long getBillGroupToken() {
		return billGroupToken;
	}

	public void setBillGroupToken(Long billGroupToken) {
		this.billGroupToken = billGroupToken;
	}
}
