package com.everhomes.contract;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.asset.szwwyjf.SZWQuery;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.contract.AddContractTemplateCommand;
import com.everhomes.rest.contract.CheckAdminCommand;
import com.everhomes.rest.contract.ContractCategoryCommand;
import com.everhomes.rest.contract.ContractCategoryListDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractDocumentDTO;
import com.everhomes.rest.contract.ContractEventDTO;
import com.everhomes.rest.contract.ContractParamDTO;
import com.everhomes.rest.contract.ContractTemplateDTO;
import com.everhomes.rest.contract.CreateContractCommand;
import com.everhomes.rest.contract.CreatePaymentContractCommand;
import com.everhomes.rest.contract.DeleteContractCommand;
import com.everhomes.rest.contract.DeleteContractTemplateCommand;
import com.everhomes.rest.contract.DenunciationContractBillsCommand;
import com.everhomes.rest.contract.DenunciationContractCommand;
import com.everhomes.rest.contract.EnterpriseContractCommand;
import com.everhomes.rest.contract.EnterpriseContractDTO;
import com.everhomes.rest.contract.DurationParamDTO;
import com.everhomes.rest.contract.EntryContractCommand;
import com.everhomes.rest.contract.FindContractCommand;
import com.everhomes.rest.contract.GenerateContractDocumentsCommand;
import com.everhomes.rest.contract.GenerateContractNumberCommand;
import com.everhomes.rest.contract.GetContractDocumentsCommand;
import com.everhomes.rest.contract.GetContractParamCommand;
import com.everhomes.rest.contract.GetContractTemplateDetailCommand;
import com.everhomes.rest.contract.GetDurationParamCommand;
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
import com.everhomes.rest.contract.UpdateContractDocumentsCommand;
import com.everhomes.rest.contract.UpdateContractTemplateCommand;
import com.everhomes.rest.contract.UpdatePaymentContractCommand;
import com.everhomes.rest.contract.listContractTemplateCommand;
import com.everhomes.rest.openapi.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;

/**
 * Created by 杨崇鑫  on 2018/4/18.
 */
@Component(ContractService.CONTRACT_PREFIX + 999966)
public class SZWContractHandler implements ContractService{
    private final static Logger LOGGER = LoggerFactory.getLogger(SZWContractHandler.class);
    
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    @Autowired
    private SZWQuery szwQuery;

	@Override
	public ListContractsResponse listContracts(ListContractsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void contractSchedule() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListContractsResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findCustomerByContractNum(String contractNum, Long ownerId, String ownerType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractDetailDTO createContract(CreateContractCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractDetailDTO updateContract(UpdateContractCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void denunciationContract(DenunciationContractCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteContract(DeleteContractCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ContractDTO> listCustomerContracts(ListCustomerContractsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractDTO> listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractDTO> listIndividualCustomerContracts(ListIndividualCustomerContractsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractDTO> listApartmentContracts(ListApartmentContractsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void entryContract(EntryContractCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reviewContract(ReviewContractCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContractParam(SetContractParamCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContractParamDTO getContractParam(GetContractParamCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateContractNumber(GenerateContractNumberCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String syncContractsFromThirdPart(SyncContractsFromThirdPartCommand cmd, Boolean authFlag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractDetailDTO updatePaymentContract(UpdatePaymentContractCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractDetailDTO createPaymentContract(CreatePaymentContractCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrganizationDTO> getUserGroups(GetUserGroupsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListContractsBySupplierResponse listContractsBySupplier(ListContractsBySupplierCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ContractDetailDTO findContractForApp(FindContractCommand cmd) {
		CheckAdminCommand cmd1 = new CheckAdminCommand();
        cmd1.setNamespaceId(cmd.getNamespaceId());
        cmd1.setOrganizationId(cmd.getOrganizationId());
        if(checkAdmin(cmd1)) {
            return findContract(cmd);
        }
        return null;
        //return findContract(cmd);//用于测试
	}
	
	@Override
	public Boolean checkAdmin(CheckAdminCommand cmd) {
		Long userId = UserContext.currentUserId();
        ListServiceModuleAdministratorsCommand cmd1 = new ListServiceModuleAdministratorsCommand();
        cmd1.setOrganizationId(cmd.getOrganizationId());
        cmd1.setActivationFlag((byte) 1);
        cmd1.setOwnerType("EhOrganizations");
        cmd1.setOwnerId(null);
        LOGGER.info("organization manager check for bill display, cmd = " + cmd1.toString());
        List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd1);
        LOGGER.info("organization manager check for bill display, orgContactsDTOs are = " + organizationContactDTOS.toString());
        LOGGER.info("organization manager check for bill display, userId = " + userId);
        for (OrganizationContactDTO dto : organizationContactDTOS) {
            Long targetId = dto.getTargetId();
            if (targetId.longValue() == userId.longValue()) {
                return true;
            }
        }
        return false;
	}
	
	@Override
	public ContractDetailDTO findContract(FindContractCommand cmd) {
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("szw findContract. cmd:{}", StringHelper.toJsonString(cmd));
        }
		String request = cmd.getContractNumber();
		ContractDetailDTO response = szwQuery.findContractForApp(request);
		return response;
	}

	@Override
	public void exportContractListByCommunityCategoryId(SearchContractCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContractTemplateDTO addContractTemplate(AddContractTemplateCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractTemplateDTO updateContractTemplate(UpdateContractTemplateCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListContractTemplatesResponse searchContractTemplates(listContractTemplateCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractDTO setPrintContractTemplate(SetPrintContractTemplateCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractDTO getContractTemplateDetail(GetContractTemplateDetailCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteContractTemplate(DeleteContractTemplateCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Long> checkPrintPreviewprivilege(PrintPreviewPrivilegeCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}


	public DurationParamDTO getDuration(GetDurationParamCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<ContractEventDTO> listContractEvents(ListContractEventsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnterpriseContractDTO EnterpriseContractDetail(EnterpriseContractCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePrintContractTemplate(SetPrintContractTemplateCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ContractCategoryListDTO> getContractCategoryList(ContractCategoryCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	public void dealBillsGeneratedByDenunciationContract(DenunciationContractBillsCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ContractDTO> getApartmentRentalContract(ListApartmentContractsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateContractDocuments(GenerateContractDocumentsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContractDocumentDTO getContractDocuments(GetContractDocumentsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateContractDocuments(UpdateContractDocumentsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

}
