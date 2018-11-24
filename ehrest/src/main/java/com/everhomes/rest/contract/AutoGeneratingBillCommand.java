package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class AutoGeneratingBillCommand {
	
	private Integer namespaceId;
	private String contractIds;

	public String getContractIds() {
		return contractIds;
	}

	public void setContractIds(String contractIds) {
		this.contractIds = contractIds;
	}

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
