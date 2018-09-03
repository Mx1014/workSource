package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

public class CaculateBuildingAreaCommand {

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
