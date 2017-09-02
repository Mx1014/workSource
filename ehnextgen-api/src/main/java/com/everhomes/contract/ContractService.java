// @formatter:off
package com.everhomes.contract;

import com.everhomes.rest.contract.*;

import java.util.List;

import java.util.List;

public interface ContractService {


	public ListContractsResponse listContracts(ListContractsCommand cmd);

	void contractSchedule();

	ListContractsResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd);


    List<Object> findCustomerByContractNum(String contractNum,Long ownerId,String ownerType);

	ContractDetailDTO createContract(CreateContractCommand cmd);
	ContractDTO updateContract(UpdateContractCommand cmd);

	void denunciationContract(DenunciationContractCommand cmd);
	ContractDetailDTO findContract(FindContractCommand cmd);
	void deleteContract(DeleteContractCommand cmd);
	List<ContractDTO> listCustomerContracts(ListCustomerContractsCommand cmd);
	List<ContractDTO> listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd);
	List<ContractDTO> listIndividualCustomerContracts(ListIndividualCustomerContractsCommand cmd);

	void entryContract(EntryContractCommand cmd);
	void reviewContract(ReviewContractCommand cmd);

	void setContractParam(SetContractParamCommand cmd);
	ContractParamDTO getContractParam(GetContractParamCommand cmd);


}