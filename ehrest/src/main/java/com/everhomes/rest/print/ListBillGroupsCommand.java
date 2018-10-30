package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId : 域空间id</li>
 * <li>chargeAppToken : 收费应用的token</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月20日
 */
public class ListBillGroupsCommand {
    private Integer namespaceId;
    private Long chargeAppToken;
    
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

}
