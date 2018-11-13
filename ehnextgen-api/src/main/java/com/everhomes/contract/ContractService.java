// @formatter:off
package com.everhomes.contract;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.everhomes.rest.contract.*;
import com.everhomes.rest.openapi.OrganizationDTO;
import com.everhomes.rest.varField.FieldDTO;

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
    //合同模板
	ContractTemplateDTO addContractTemplate(AddContractTemplateCommand cmd);
	ContractTemplateDTO updateContractTemplate(UpdateContractTemplateCommand cmd);
	ListContractTemplatesResponse searchContractTemplates(listContractTemplateCommand cmd);
	ContractDTO setPrintContractTemplate(SetPrintContractTemplateCommand cmd);
	ContractDTO getContractTemplateDetail(GetContractTemplateDetailCommand cmd);
	void deleteContractTemplate(DeleteContractTemplateCommand cmd);
	List<Long> checkPrintPreviewprivilege(PrintPreviewPrivilegeCommand cmd);

	DurationParamDTO getDuration(GetDurationParamCommand cmd);

	default Byte filterAptitudeCustomer(FilterAptitudeCustomerCommand cmd){
		return null;
	}

	default AptitudeCustomerFlagDTO updateAptitudeCustomer(UpdateContractAptitudeFlagCommand cmd){
		return null;
	}

	default OutputStream exportOutputStreamContractListByContractList(SearchContractCommand cmd, Long taskId){return null;}

	default void exportContractListByContractList(SearchContractCommand cmd){}
   

	public List<ContractEventDTO> listContractEvents(ListContractEventsCommand cmd);

	public EnterpriseContractDTO EnterpriseContractDetail(EnterpriseContractCommand cmd);
	void deletePrintContractTemplate(SetPrintContractTemplateCommand cmd);

	List<ContractCategoryListDTO> getContractCategoryList(ContractCategoryCommand cmd);

	void dealBillsGeneratedByDenunciationContract(DenunciationContractBillsCommand cmd);
	//导出对接下载中心
	void exportContractListByCommunityCategoryId(SearchContractCommand cmd);
	default OutputStream exportOutputStreamListByTaskId(SearchContractCommand cmd, Long taskId){return null;}
	default ExcelPropertyInfo exportPropertyInfo(Map<String, String> customFields, List<FieldDTO> dynamicField, String[] exportfield, int[] customFieldtitleSizes){return null;}
   
	default ContractTaskOperateLog initializationContract(InitializationCommand cmd){return null;};
	default ContractTaskOperateLog exemptionContract(InitializationCommand cmd){return null;};
	default void copyContract(InitializationCommand cmd){};
	default SearchProgressDTO findContractOperateTaskById(SearchProgressCommand cmd){return null;};
}