package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class ListContractEventsCommand {
	
	private Long contractId;
	
	public ListContractEventsCommand() {
		
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
