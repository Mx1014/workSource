package com.everhomes.contract;

import com.everhomes.flow.*;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.search.ContractSearcher;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/21.
 */
@Component
public class ContractFlowModuleListener implements FlowModuleListener {
    private static final long MODULE_ID = 21200L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractFlowModuleListener.class);

    @Autowired
    private FlowService flowService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        FlowModuleDTO moduleDTO = flowService.getModuleById(MODULE_ID);
        module.setModuleName(moduleDTO.getDisplayName());
        module.setModuleId(MODULE_ID);
        return module;
    }

    @Override
    public void onFlowCreating(Flow flow) {

    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("step into onFlowCaseEnd, ctx: {}", ctx);
        }
        FlowCase flowCase = ctx.getFlowCase();
        Contract contract = contractProvider.findContractById(flowCase.getReferId());
        if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
            contract.setStatus(ContractStatus.APPROVE_NOT_QUALITIED.getCode());
            contractProvider.updateContract(contract);
            contractSearcher.feedDoc(contract);
            dealAddressLivingStatus(contract, AddressMappingStatus.FREE.getCode());
        }else if(ContractStatus.DENUNCIATION.equals(ContractStatus.fromStatus(contract.getStatus()))) {
        }
    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("step into onFlowCaseEnd, ctx: {}", ctx);
        }
        FlowCase flowCase = ctx.getFlowCase();
        Contract contract = contractProvider.findContractById(flowCase.getReferId());

        if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
            contract.setStatus(ContractStatus.APPROVE_QUALITIED.getCode());
            contractProvider.updateContract(contract);
            contractSearcher.feedDoc(contract);
        } else if(ContractStatus.DENUNCIATION.equals(ContractStatus.fromStatus(contract.getStatus()))) {
            dealAddressLivingStatus(contract, AddressMappingStatus.FREE.getCode());
        }

    }

    private void dealAddressLivingStatus(Contract contract, byte livingStatus) {
        List<ContractBuildingMapping> mappings = contractBuildingMappingProvider.listByContract(contract.getId());
        mappings.forEach(mapping -> {
            CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(mapping.getAddressId());
            addressMapping.setLivingStatus(livingStatus);
            propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
        });
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase) {
        return null;
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {

    }

    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {

    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) {

    }
}
