// @formatter:off
package com.everhomes.contract;

import com.everhomes.rest.contract.*;

public interface ContractService {


	public ListContractsResponse listContracts(ListContractsCommand cmd);

	void contractSchedule();

	ListContractsResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd);

	void createContract(CreateContractCommand cmd);

	void denunciationContract(DenunciationContractCommand cmd);
	ContractDetailDTO findContract(FindContractCommand cmd);

}