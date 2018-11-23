package com.everhomes.rest.contract;

import java.util.List;

import com.everhomes.util.StringHelper;

public class InitializationCommand {
	
	private Integer namespaceId;
	private Long communityId;
	List<Long> contractIds;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public List<Long> getContractIds() {
		return contractIds;
	}

	public void setContractIds(List<Long> contractIds) {
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
