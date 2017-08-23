// @formatter:off
package com.everhomes.contract;

import com.everhomes.rest.contract.*;

import java.util.List;

public interface ContractService {


	public ListContractsResponse listContracts(ListContractsCommand cmd);

	void contractSchedule();

	ListContractsResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd);

	ContractDetailDTO createContract(CreateContractCommand cmd);
	ContractDTO updateContract(UpdateContractCommand cmd);

	void denunciationContract(DenunciationContractCommand cmd);
	ContractDetailDTO findContract(FindContractCommand cmd);
	void deleteContract(DeleteContractCommand cmd);
	List<ContractDTO> listCustomerContracts(ListCustomerContractsCommand cmd);
	List<ContractDTO> listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd);

	void entryContract(EntryContractCommand cmd);

}