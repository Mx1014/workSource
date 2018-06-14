package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class GetDurationParamCommand {
	
	private Long endTimeByDay;
	private Long contractId;
	
	public Long getEndTimeByDay() {
		return endTimeByDay;
	}
	public void setEndTimeByDay(Long endTimeByDay) {
		this.endTimeByDay = endTimeByDay;
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
