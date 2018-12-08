package com.everhomes.contract.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.ContractDetailDTO;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "chargingChanges")
public class ChargingChangesContractTemplate implements ContractTemplateHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(ChargingChangesContractTemplate.class);
	
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		// TODO Auto-generated method stub
		return null;
	}

}
