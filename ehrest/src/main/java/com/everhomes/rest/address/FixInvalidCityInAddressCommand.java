package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class FixInvalidCityInAddressCommand {
	private Integer namespaceId;
	
	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
