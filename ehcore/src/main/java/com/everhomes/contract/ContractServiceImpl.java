// @formatter:off
package com.everhomes.contract;

import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.ListContractsCommand;
import com.everhomes.rest.contract.ListContractsResponse;

@Component
public class ContractServiceImpl implements ContractService {

	@Override
	public ListContractsResponse listContracts(ListContractsCommand cmd) {
		
		return new ListContractsResponse();
	}

}