// @formatter:off
package com.everhomes.contract;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.contract.AddContractTemplateCommand;
import com.everhomes.rest.contract.CheckAdminCommand;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractEventDTO;
import com.everhomes.rest.contract.ContractParamDTO;
import com.everhomes.rest.contract.ContractTemplateDTO;
import com.everhomes.rest.contract.CreateContractCommand;
import com.everhomes.rest.contract.CreatePaymentContractCommand;
import com.everhomes.rest.contract.DeleteContractCommand;
import com.everhomes.rest.contract.DeleteContractTemplateCommand;
import com.everhomes.rest.contract.DenunciationContractCommand;
import com.everhomes.rest.contract.EntryContractCommand;
import com.everhomes.rest.contract.FindContractCommand;
import com.everhomes.rest.contract.GenerateContractNumberCommand;
import com.everhomes.rest.contract.GetContractParamCommand;
import com.everhomes.rest.contract.GetContractTemplateDetailCommand;
import com.everhomes.rest.contract.GetUserGroupsCommand;
import com.everhomes.rest.contract.ListApartmentContractsCommand;
import com.everhomes.rest.contract.ListContractEventsCommand;
import com.everhomes.rest.contract.ListContractTemplatesResponse;
import com.everhomes.rest.contract.ListContractsByOraganizationIdCommand;
import com.everhomes.rest.contract.ListContractsBySupplierCommand;
import com.everhomes.rest.contract.ListContractsBySupplierResponse;
import com.everhomes.rest.contract.ListContractsCommand;
import com.everhomes.rest.contract.ListContractsResponse;
import com.everhomes.rest.contract.ListCustomerContractsCommand;
import com.everhomes.rest.contract.ListEnterpriseCustomerContractsCommand;
import com.everhomes.rest.contract.ListIndividualCustomerContractsCommand;
import com.everhomes.rest.contract.PrintPreviewPrivilegeCommand;
import com.everhomes.rest.contract.ReviewContractCommand;
import com.everhomes.rest.contract.SearchContractCommand;
import com.everhomes.rest.contract.SetContractParamCommand;
import com.everhomes.rest.contract.SetPrintContractTemplateCommand;
import com.everhomes.rest.contract.SyncContractsFromThirdPartCommand;
import com.everhomes.rest.contract.UpdateContractCommand;
import com.everhomes.rest.contract.UpdateContractTemplateCommand;
import com.everhomes.rest.contract.UpdatePaymentContractCommand;
import com.everhomes.rest.contract.listContractTemplateCommand;
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
   
}