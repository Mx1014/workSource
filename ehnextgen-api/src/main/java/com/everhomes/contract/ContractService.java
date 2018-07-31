// @formatter:off
package com.everhomes.contract;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.contract.*;
import com.everhomes.rest.openapi.OrganizationDTO;

public interface ContractService {
	String CONTRACT_PREFIX = "contract-";

	public ListContractsResponse listContracts(ListContractsCommand cmd);

	void contractSchedule();

	ListContractsResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd);


    List<Object> findCustomerByContractNum(String contractNum,Long ownerId,String ownerType);

	ContractDetailDTO createContract(CreateContractCommand cmd);
	ContractDetailDTO updateContract(UpdateContractCommand cmd);

	void denunciationContract(DenunciationContractCommand cmd);
	ContractDetailDTO findContract(FindContractCommand cmd);
	void deleteContract(DeleteContractCommand cmd);
	List<ContractDTO> listCustomerContracts(ListCustomerContractsCommand cmd);
	List<ContractDTO> listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd);
	List<ContractDTO> listIndividualCustomerContracts(ListIndividualCustomerContractsCommand cmd);
	List<ContractDTO> listApartmentContracts(ListApartmentContractsCommand cmd);


	void entryContract(EntryContractCommand cmd);
	void reviewContract(ReviewContractCommand cmd);

	void setContractParam(SetContractParamCommand cmd);
	ContractParamDTO getContractParam(GetContractParamCommand cmd);

	String generateContractNumber(GenerateContractNumberCommand cmd);
	String syncContractsFromThirdPart(SyncContractsFromThirdPartCommand cmd, Boolean authFlag);

	Boolean checkAdmin(CheckAdminCommand cmd);
	ContractDetailDTO findContractForApp(FindContractCommand cmd);

	ContractDetailDTO updatePaymentContract(UpdatePaymentContractCommand cmd);
	ContractDetailDTO createPaymentContract(CreatePaymentContractCommand cmd);

	List<OrganizationDTO> getUserGroups(GetUserGroupsCommand cmd);

    ListContractsBySupplierResponse listContractsBySupplier(ListContractsBySupplierCommand cmd);
    
    //add by jm.ding
	void exportContractListByCommunityCategoryId(SearchContractCommand cmd, HttpServletResponse response);
	ContractTemplateDTO addContractTemplate(AddContractTemplateCommand cmd);
	ContractTemplateDTO updateContractTemplate(UpdateContractTemplateCommand cmd);
	ListContractTemplatesResponse searchContractTemplates(listContractTemplateCommand cmd);
	ContractDTO setPrintContractTemplate(SetPrintContractTemplateCommand cmd);
	ContractDTO getContractTemplateDetail(GetContractTemplateDetailCommand cmd);
	void deleteContractTemplate(DeleteContractTemplateCommand cmd);
	List<Long> checkPrintPreviewprivilege(PrintPreviewPrivilegeCommand cmd);

    //add by tangcen
	public List<ContractEventDTO> listContractEvents(ListContractEventsCommand cmd);

	default Byte filterAptitudeCustomer(FilterAptitudeCustomerCommand cmd){
		return null;
	}

	default void updateAptitudeCustomer(UpdateContractAptitudeFlagCommand cmd){

	}
   
}