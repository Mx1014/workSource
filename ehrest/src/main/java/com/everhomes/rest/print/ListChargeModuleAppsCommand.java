package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId : namespaceId</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月20日
 */
public class ListChargeModuleAppsCommand {
	private Integer namespaceId;
	
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
}
