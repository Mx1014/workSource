package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class GetDurationParamCommand {
	
	private Long endTimeByDay;
	private Long ContractId;
	
	public Long getEndTimeByDay() {
		return endTimeByDay;
	}
	public void setEndTimeByDay(Long endTimeByDay) {
		this.endTimeByDay = endTimeByDay;
	}
	public Long getContractId() {
		return ContractId;
	}
	public void setContractId(Long contractId) {
		ContractId = contractId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
