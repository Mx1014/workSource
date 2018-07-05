package com.everhomes.contract;


import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.asset.szwwyjf.SZWQuery;
import com.everhomes.community.Community;
import com.everhomes.openapi.Contract;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.openapi.OrganizationDTO;
import com.everhomes.rest.openapi.shenzhou.ZJContractDetail;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<ContractEventDTO> listContractEvents(ListContractEventsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
