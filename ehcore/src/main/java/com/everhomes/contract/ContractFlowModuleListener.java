package com.everhomes.contract;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.flow.*;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractApplicationScene;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.contract.ContractTrackingTemplateCode;
import com.everhomes.rest.contract.ContractType;
import com.everhomes.rest.contract.FindContractCommand;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private ContractProvider contractProvider;

    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;
    
    @Autowired
	private EnterpriseCustomerProvider enterpriseCustomerProvider;
    
    @Autowired
	private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        List<FlowServiceTypeDTO> list = new ArrayList<>();
        FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
        List<Long> moduleIds = new ArrayList<>();
        moduleIds.add(21200L);
        List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(namespaceId, moduleIds);
        if(apps != null && apps.size() > 0) {
            dto.setNamespaceId(namespaceId);
            dto.setId(null);
            dto.setServiceName(apps.get(0).getName());
            list.add(dto);
        }
        return list;
    }

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
            LOGGER.debug("step into onFlowCaseAbsorted, ctx: {}", ctx);
        }
        FlowCase flowCase = ctx.getFlowCase();
        
        Contract contract = contractProvider.findContractById(flowCase.getReferId());
        //查询合同适用场景，物业合同不修改资产状态。
        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
        
        if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
            contract.setStatus(ContractStatus.APPROVE_NOT_QUALITIED.getCode());
            contractProvider.updateContract(contract);
            contractSearcher.feedDoc(contract);
            //审批不通过的续约合同，变更合同不修改资产状态
			if (!ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))
					&& ContractType.NEW.equals(ContractType.fromStatus(contract.getContractType()))) {
				dealAddressLivingStatus(contract, AddressMappingStatus.FREE.getCode());
			}
        }else if(ContractStatus.DENUNCIATION.equals(ContractStatus.fromStatus(contract.getStatus()))) {

        }
    }

    @Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("step into onFlowCaseEnd, ctx: {}", ctx);
		}
		FlowCase flowCase = ctx.getFlowCase();
		if (ctx.getStepType() == FlowStepType.REJECT_STEP && FlowNodeType.START.getCode().equals(ctx.getCurrentNode().getFlowNode().getNodeType())) {
			// 审批驳回开始节点，更新合同的状态为待发起 -- djm
			Contract contract = contractProvider.findContractById(flowCase.getReferId());
			contract.setStatus(ContractStatus.WAITING_FOR_LAUNCH.getCode());
			contractProvider.updateContract(contract);
			contractSearcher.feedDoc(contract);
		}
	}

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("step into onFlowCaseEnd, ctx: {}", ctx);
        }
        FlowCase flowCase = ctx.getFlowCase();
        Contract contract = contractProvider.findContractById(flowCase.getReferId());
        Contract exist = contractProvider.findContractById(flowCase.getReferId());
        //因为异常终止也会进FlowCaseEnd，所以需要再判断一下是不是正常结束 by xiongying20170908
        //查询合同适用场景，物业合同不修改资产状态。
        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
        
        if(FlowStepType.APPROVE_STEP.equals(ctx.getStepType()) || FlowStepType.END_STEP.equals(ctx.getStepType())) {
            if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
                contract.setStatus(ContractStatus.APPROVE_QUALITIED.getCode());
                contractProvider.updateContract(contract);
                contractSearcher.feedDoc(contract);
                //记录合同事件日志，by tangcen
        		contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_UPDATE,contract,exist);
            } else if(ContractStatus.DENUNCIATION.equals(ContractStatus.fromStatus(contract.getStatus())) && !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))) {
                dealAddressLivingStatus(contract, AddressMappingStatus.FREE.getCode());
              //查询企业客户信息，客户状态会由已成交客户变为历史客户
        		if (contract.getCustomerType()==0) {
        			EnterpriseCustomer enterpriseCustomer = enterpriseCustomerProvider.findById(contract.getCustomerId());
        			enterpriseCustomer.setLevelItemId(7L);
        			enterpriseCustomerProvider.updateEnterpriseCustomer(enterpriseCustomer);
        			enterpriseCustomerSearcher.feedDoc(enterpriseCustomer);
        		}
            }
        }
    }

    private void dealAddressLivingStatus(Contract contract, byte livingStatus) {
        List<ContractBuildingMapping> mappings = contractBuildingMappingProvider.listByContract(contract.getId());
        mappings.forEach(mapping -> {
            CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(mapping.getAddressId());
            if(!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(addressMapping.getLivingStatus()))) {
                addressMapping.setLivingStatus(livingStatus);
                propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
            }
        });
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @SuppressWarnings("unchecked")
	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		FindContractCommand cmd = new FindContractCommand();
		cmd.setId(flowCase.getReferId());
		cmd.setNamespaceId(flowCase.getNamespaceId());
		cmd.setCommunityId(flowCase.getProjectId());
		cmd.setCategoryId(flowCase.getOwnerId());

		ContractService contractService = getContractService(flowCase.getNamespaceId());
		ContractDetailDTO contractDetailDTO = contractService.findContract(cmd);

		flowCase.setCustomObject(JSONObject.toJSONString(contractDetailDTO));

		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e;
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey("合同编号");
		e.setValue(contractDetailDTO.getContractNumber());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同名称");
		e.setValue(contractDetailDTO.getName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("客户名称");
		e.setValue(contractDetailDTO.getCustomerName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同开始时间");
		e.setValue(timeToStr(contractDetailDTO.getContractStartDate()));
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同结束时间");
		e.setValue(timeToStr(contractDetailDTO.getContractEndDate()));
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("经办人");
		e.setValue(contractDetailDTO.getCreatorName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("签约日期");
		e.setValue(timeToStr(contractDetailDTO.getSignedTime()));
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同资产");
		StringBuffer apartments = new StringBuffer();

		for (BuildingApartmentDTO apartment : contractDetailDTO.getApartments()) {
			apartments.append(apartment.getBuildingName() + "-" + apartment.getApartmentName() + "  面积："
					+ apartment.getChargeArea() + "，"); 
		}
		e.setValue((apartments.toString()).substring(0, (apartments.toString()).length()-1));
		entities.add(e);

		for (int i = 0; i < (contractDetailDTO.getChargingItems()).size(); i++) {
			
			List<FlowCaseEntity> chargingItemEntities = new ArrayList<>();
			FlowCaseEntity chargingItemeE;
			
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.CONTRACT_PRICE.getCode());
			e.setKey("计价条款");
			
			// 计价条款json 转对象
			if (contractDetailDTO.getChargingItems().get(i).getChargingVariables() != null) {
				contractDetailDTO.getChargingItems().get(i).getChargingVariables();
				chargingItemeE = new FlowCaseEntity();
				chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
				chargingItemeE.setKey("收费项目");
				chargingItemeE.setValue(contractDetailDTO.getChargingItems().get(i).getChargingItemName());
				chargingItemEntities.add(chargingItemeE);
				Map json = (Map) JSONObject.parse(contractDetailDTO.getChargingItems().get(i).getChargingVariables());
				StringBuffer FormulaVariable = new StringBuffer();
				for (Object map : json.entrySet()) {
					List<Map<String, String>> ChargingVariables = (List<Map<String, String>>) ((Map.Entry) map).getValue();
					for (int j = 0; j < ChargingVariables.size(); j++) {
						FormulaVariable.append( ChargingVariables.get(j).get("variableName") + "："
								+ String.valueOf(ChargingVariables.get(j).get("variableValue")));
					}
				}
				chargingItemeE = new FlowCaseEntity();
				chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
				chargingItemeE.setKey("计费公式");
				chargingItemeE.setValue(contractDetailDTO.getChargingItems().get(i).getFormula() +"("+ FormulaVariable+")");
				chargingItemEntities.add(chargingItemeE);
				
				if (contractDetailDTO.getChargingItems().get(i).getChargingStartTime() != null) {
					chargingItemeE = new FlowCaseEntity();
					chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
					chargingItemeE.setKey("起计日期");
					chargingItemeE.setValue(timeToStr2(contractDetailDTO.getChargingItems().get(i).getChargingStartTime()));
					chargingItemEntities.add(chargingItemeE);
					
					chargingItemeE = new FlowCaseEntity();
					chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
					chargingItemeE.setKey("截至日期");
					chargingItemeE.setValue(timeToStr2(contractDetailDTO.getChargingItems().get(i).getChargingExpiredTime()));
					chargingItemEntities.add(chargingItemeE);  
				}
				// 添加应用的资产
				StringBuffer apartmentVariable = new StringBuffer();
				contractDetailDTO.getChargingItems().get(i).getApartments();
				for (BuildingApartmentDTO apartment : contractDetailDTO.getChargingItems().get(i).getApartments()) {
					apartmentVariable.append(apartment.getBuildingName() + "-" + apartment.getApartmentName() + "， ");
				}
				chargingItemeE = new FlowCaseEntity();
				chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
				chargingItemeE.setKey("应用资产");
				chargingItemeE.setValue(apartmentVariable.toString());
				chargingItemEntities.add(chargingItemeE); 
			}
			e.setChargingItemEntities(chargingItemEntities);
			entities.add(e);
		}
		return entities;
	}
    private ContractService getContractService(Integer namespaceId) {
        String handler = configurationProvider.getValue(namespaceId, "contractService", "");
        return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
    }

    private String timeToStr(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }
    private String timeToStr2(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
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
