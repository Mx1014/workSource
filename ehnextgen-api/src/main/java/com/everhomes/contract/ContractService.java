// @formatter:off
package com.everhomes.contract;

import com.everhomes.rest.contract.ListContractsByOraganizationIdCommand;
import com.everhomes.rest.contract.ListContractsCommand;
import com.everhomes.rest.contract.ListContractsResponse;

import java.util.List;

public interface ContractService {


	public ListContractsResponse listContracts(ListContractsCommand cmd);

	void contractSchedule();

	ListContractsResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd);

    List<Object> findCustomerByContractNum(String contractNum);
}