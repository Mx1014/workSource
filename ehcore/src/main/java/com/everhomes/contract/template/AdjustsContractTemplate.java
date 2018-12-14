package com.everhomes.contract.template;

import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.ContractDetailDTO;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "adjusts")
public class AdjustsContractTemplate extends ChargingChangesContractTemplate implements ContractTemplateHandler {

	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		return super.isValid(contract, segments, "adjusts");
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		return super.getValue(contract, segments, "adjusts");
	}

}
