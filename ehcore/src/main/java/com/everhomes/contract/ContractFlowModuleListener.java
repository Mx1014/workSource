package com.everhomes.contract;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.customer.CustomerEntryInfo;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.flow.*;
import com.everhomes.flow.conditionvariable.FlowConditionNumberVariable;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.asset.ChargingVariable;
import com.everhomes.rest.asset.ChargingVariables;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    @Autowired
    private AssetService assetService;
    
    @Autowired
	private AssetProvider assetProvider;
    
    @Autowired
    private AddressProvider addressProvider;
    
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
            } else if(ContractStatus.DENUNCIATION.equals(ContractStatus.fromStatus(contract.getStatus()))){
            	if (!ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))) {
	            	 dealAddressLivingStatus(contract, AddressMappingStatus.FREE.getCode());
	                 //查询企业客户信息，客户状态会由已成交客户变为历史客户
	           		 if (contract.getCustomerType()==0) {
	           			EnterpriseCustomer enterpriseCustomer = enterpriseCustomerProvider.findById(contract.getCustomerId());
	           			enterpriseCustomer.setLevelItemId(7L);
	           			enterpriseCustomerProvider.updateEnterpriseCustomer(enterpriseCustomer);
	           			enterpriseCustomerSearcher.feedDoc(enterpriseCustomer);
	           		 }
				} 
            	//add by tangcen 退约合同审批通过后，对该合同未出的账单进行处理
        		if (contract.getCostGenerationMethod()!=null) {
        			assetService.deleteUnsettledBillsOnContractId(contract.getCostGenerationMethod(),contract.getId(),contract.getDenunciationTime());
        			
        			if(contract.getCategoryId() == null){
        				contract.setCategoryId(0l);
			        }else {
			        	// 转换
			            Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l, contract.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
			            contract.setCategoryId(assetCategoryId);
					}
        			
        			BigDecimal totalAmount = assetProvider.getBillExpectanciesAmountOnContract(contract.getContractNumber(),contract.getId(), contract.getCategoryId(), contract.getNamespaceId());
        			contract.setRent(totalAmount);
        			contractProvider.updateContract(contract);
                    contractSearcher.feedDoc(contract);
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
            //#37329 企业客户管理-入驻信息tab里面关联的该楼栋门牌清理掉
            List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listAddressEntryInfos(mapping.getAddressId());
            entryInfos.forEach(entryInfo -> {
            	CustomerEntryInfo customerEntryInfo = enterpriseCustomerProvider.findCustomerEntryInfoById(entryInfo.getId());
            	customerEntryInfo.setStatus(CommonStatus.INACTIVE.getCode());
            	enterpriseCustomerProvider.updateCustomerEntryInfo(customerEntryInfo);
                
            });
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
		//需要在后面添加数据，所以放到后面
		//flowCase.setCustomObject(JSONObject.toJSONString(contractDetailDTO));

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

		if (contractDetailDTO.getSignedTime() != null) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("签约日期");
			e.setValue(timeToStr(contractDetailDTO.getSignedTime()));
			entities.add(e);
		}

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同资产");
		StringBuffer apartments = new StringBuffer();

		for (BuildingApartmentDTO apartment : contractDetailDTO.getApartments()) {
			apartments.append(apartment.getBuildingName() + "-" + apartment.getApartmentName()/*+ "  面积："
					+ apartment.getChargeArea()*/ + "，"); 
		}
		e.setValue((apartments.toString()).substring(0, (apartments.toString()).length()-1));
		entities.add(e);

		List<FlowCaseEntity> entities1 = new ArrayList<>();
		for (int i = 0; i < (contractDetailDTO.getChargingItems()).size(); i++) {
			List<FlowCaseEntity> chargingItemEntities = new ArrayList<>();
			FlowCaseEntity chargingItemeE;
			
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.CONTRACT_PRICE.getCode());
			e.setKey("计价条款");
			e.setValue(" ");
			
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
					chargingItemeE.setKey("截止日期");
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
			entities1.add(e);
		}
		//app使用
		contractDetailDTO.setEntities(entities);
		contractDetailDTO.setPrice(entities1);
		flowCase.setCustomObject(JSONObject.toJSONString(contractDetailDTO));
		
		for (int i = 0; i < (contractDetailDTO.getChargingItems()).size(); i++) {
			
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("计价条款");
			e.setValue(" ");
			entities.add(e);
			
			if (contractDetailDTO.getChargingItems().get(i).getChargingVariables() != null) {
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.LIST.getCode());
				e.setKey("收费项目");
				e.setValue(contractDetailDTO.getChargingItems().get(i).getChargingItemName());
				entities.add(e);
				
				Map json = (Map) JSONObject.parse(contractDetailDTO.getChargingItems().get(i).getChargingVariables());
				StringBuffer FormulaVariable = new StringBuffer();
				for (Object map : json.entrySet()) {
					List<Map<String, String>> ChargingVariables = (List<Map<String, String>>) ((Map.Entry) map).getValue();
					for (int j = 0; j < ChargingVariables.size(); j++) {
						FormulaVariable.append( ChargingVariables.get(j).get("variableName") + "："
								+ String.valueOf(ChargingVariables.get(j).get("variableValue")));
					}
				}
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.LIST.getCode());
				e.setKey("计费公式");
				e.setValue(contractDetailDTO.getChargingItems().get(i).getFormula() +"("+ FormulaVariable+")");
				entities.add(e);
				
				if (contractDetailDTO.getChargingItems().get(i).getChargingStartTime() != null) {
					e = new FlowCaseEntity();
					e.setEntityType(FlowCaseEntityType.LIST.getCode());
					e.setKey("起计日期");
					e.setValue(timeToStr2(contractDetailDTO.getChargingItems().get(i).getChargingStartTime()));
					entities.add(e);
					
					e = new FlowCaseEntity();
					e.setEntityType(FlowCaseEntityType.LIST.getCode());
					e.setKey("截止日期");
					e.setValue(timeToStr2(contractDetailDTO.getChargingItems().get(i).getChargingExpiredTime()));
					entities.add(e);
				}
				// 添加应用的资产
				StringBuffer apartmentVariable = new StringBuffer();
				contractDetailDTO.getChargingItems().get(i).getApartments();
				for (BuildingApartmentDTO apartment : contractDetailDTO.getChargingItems().get(i).getApartments()) {
					apartmentVariable.append(apartment.getBuildingName() + "-" + apartment.getApartmentName() + "，");
				}
				
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.LIST.getCode());
				e.setKey("应用资产");
				e.setValue((apartmentVariable.toString()).substring(0, (apartmentVariable.toString()).length()-1));
				entities.add(e);
			}
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
	public List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {

		List<FlowConditionVariableDTO> list = new ArrayList<>();
		FlowConditionVariableDTO dto = new FlowConditionVariableDTO();
		dto.setDisplayName("合同房价");
		dto.setValue("contractApartPrice");
		dto.setFieldType(GeneralFormFieldType.NUMBER_TEXT.getCode());
		// dto.setOperators(new ArrayList<>());
		// dto.getOperators().add(FlowConditionRelationalOperatorType.EQUAL.getCode());
		list.add(dto);

		dto = new FlowConditionVariableDTO();
		dto.setDisplayName("资产房间");
		dto.setValue("ApartPrice");
		dto.setFieldType(GeneralFormFieldType.NUMBER_TEXT.getCode());
		// dto.setOperators(new ArrayList<>());
		// dto.getOperators().add(FlowConditionRelationalOperatorType.EQUAL.getCode());
		// dto.setOptions(Arrays.asList("一栋", "二栋", "三栋"));
		list.add(dto);

		return list;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String entityType, Long entityId, String extra) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("step into onFlowConditionVariableRender, ctx: {}", ctx);
		}
		// 基本的信息
		FlowCase flowCase = ctx.getFlowCase();
		FindContractCommand cmd = new FindContractCommand();
		cmd.setId(flowCase.getReferId());
		cmd.setNamespaceId(flowCase.getNamespaceId());
		cmd.setCommunityId(flowCase.getProjectId());
		cmd.setCategoryId(flowCase.getOwnerId());
		ContractService contractService = getContractService(flowCase.getNamespaceId());
		ContractDetailDTO contractDetailDTO = contractService.findContract(cmd);

		// 合同产生的租赁金额
		if ("contractApartPrice".equals(variable)) {
			BigDecimal contractMonthRent = contractGetMonthRent(contractDetailDTO);
			FlowConditionNumberVariable flowConditionNumberVariable = new FlowConditionNumberVariable(contractMonthRent);
			return flowConditionNumberVariable;
		}
		
		// 房源产生的租赁金额
		if ("ApartPrice".equals(variable)) {
			BigDecimal apartmentMonthRent = apartmentGetMonthRent(contractDetailDTO);
			FlowConditionNumberVariable flowConditionNumberVariable = new FlowConditionNumberVariable(apartmentMonthRent);
			return flowConditionNumberVariable;
		}
		return null;
	}
	
	//计算合同产生的租赁金额
	public BigDecimal contractGetMonthRent(ContractDetailDTO contractDetailDTO) {
		BigDecimal totalSum = BigDecimal.ZERO; // 合同租赁总额
		if (contractDetailDTO.getChargingItems() != null) {
			for (int i = 0; i < contractDetailDTO.getChargingItems().size(); i++) {
				if (contractDetailDTO.getChargingItems().get(i).getChargingItemId() != 1) {
					return totalSum;
				}
				String chargingVariables = contractDetailDTO.getChargingItems().get(i).getChargingVariables();
				if (chargingVariables.contains("\"variableIdentifier\":\"gdje\"")) {// 固定金额
					ChargingVariables chargingVariableList = (ChargingVariables) StringHelper.fromJsonString(chargingVariables, ChargingVariables.class);
					if (chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
						for (ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
							if (chargingVariable.getVariableIdentifier() != null && chargingVariable.getVariableIdentifier().equals("gdje")
									&& contractDetailDTO.getChargingItems().get(i).getApartments() != null) {

								//一个固定金额的计价条款上面有几个房源
								BigDecimal apartmentSize = BigDecimal.valueOf(contractDetailDTO.getChargingItems().get(i).getApartments().size());
								BigDecimal gdje = BigDecimal.valueOf(Double.parseDouble(chargingVariable.getVariableValue() + ""));
								
								if (null != contractDetailDTO.getChargingItems().get(i).getBillingCycle()) {// 为null代表无此分类的应用
									switch (ApartmentRentType.fromCode(contractDetailDTO.getChargingItems().get(i).getBillingCycle())) {
									case DAY:
										totalSum = totalSum.add(gdje.multiply(apartmentSize).multiply(BigDecimal.valueOf(ApartmentRentType.DAY.getOffset())));
										break;
									case NATURAL_MONTH:
										totalSum = totalSum.add(gdje.multiply(apartmentSize));
										break;
									case NATURAL_QUARTER:
										totalSum = totalSum.add(gdje.multiply(apartmentSize).divide(BigDecimal.valueOf(ApartmentRentType.NATURAL_QUARTER.getOffset()), 2, BigDecimal.ROUND_HALF_UP));
										break;
									case NATURAL_YEAR:
										totalSum = totalSum.add(gdje.multiply(apartmentSize).divide(BigDecimal.valueOf(ApartmentRentType.NATURAL_YEAR.getOffset()), 2, BigDecimal.ROUND_HALF_UP));
										break;
									default:
										break;
									}
								} else {
									LOGGER.error("contractGetMonthRent waiting for launch contract can launch!");
									throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE,
											ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY,
											"contract status is not waiting for launch!");
								}
							}
						}
					}
				} else if (chargingVariables.contains("\"variableIdentifier\":\"dj\"")) {// 单价
					ChargingVariables chargingVariableList = (ChargingVariables) StringHelper.fromJsonString(chargingVariables, ChargingVariables.class);
					if (chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
						BigDecimal dj = BigDecimal.ZERO;// 单价
						BigDecimal mj = BigDecimal.ZERO;
						for (ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
							if (chargingVariable.getVariableIdentifier() != null) {
								if (chargingVariable.getVariableIdentifier().equals("dj")) {
									dj = BigDecimal
											.valueOf(Double.parseDouble(chargingVariable.getVariableValue() + ""));
								}
								if (chargingVariable.getVariableIdentifier().equals("mj")) {
									mj = BigDecimal
											.valueOf(Double.parseDouble(chargingVariable.getVariableValue() + ""));
								}
							}

						}
						if (null != contractDetailDTO.getChargingItems().get(i).getBillingCycle()) {// 为null代表无此分类的应用
							switch (ApartmentRentType.fromCode(contractDetailDTO.getChargingItems().get(i).getBillingCycle())) {
							case DAY:
								totalSum = totalSum.add(dj.multiply(mj).multiply(BigDecimal.valueOf(ApartmentRentType.DAY.getOffset())));
								break;
							case NATURAL_MONTH:
								totalSum = totalSum.add(dj.multiply(mj));
								break;
							case NATURAL_QUARTER:
								totalSum = totalSum.add(dj.multiply(mj).divide(BigDecimal.valueOf(ApartmentRentType.NATURAL_QUARTER.getOffset()), 2, BigDecimal.ROUND_HALF_UP));
								break;
							case NATURAL_YEAR:
								totalSum = totalSum.add(dj.multiply(mj).divide(BigDecimal.valueOf(ApartmentRentType.NATURAL_YEAR.getOffset()), 2, BigDecimal.ROUND_HALF_UP));
								break;
							default:
								break;
							}
						} else {
							LOGGER.error("only waiting for launch contract can launch!");
							throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE,
									ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY,
									"contract status is not waiting for launch!");
						}
					}
				}
			}
		}

		return totalSum;
	}
    
	// 计算房源产生的租赁金额
	public BigDecimal apartmentGetMonthRent(ContractDetailDTO contractDetailDTO) {
		BigDecimal monthRent = BigDecimal.ZERO; // 每个月的租赁总额
		// 查询合同关联房源
		if (contractDetailDTO.getApartments() != null) {
			for (int i = 0; i < contractDetailDTO.getApartments().size(); i++) {
				Long addressId = contractDetailDTO.getApartments().get(i).getAddressId();
				// 获得房源的详细信息
				Address address = addressProvider.findAddressById(addressId);
				// 房源设置的租金
				BigDecimal apartmentRent = address.getApartmentRent();
				// 房源租金类型
				address.getApartmentRentType();
				// 收费面积
				BigDecimal chargeArea = BigDecimal.valueOf(address.getChargeArea());
				if (null != address.getApartmentRentType()) {// 为null代表无此分类的应用
					switch (ApartmentRentType.fromCode(address.getApartmentRentType())) {
					case DAY:
						monthRent = monthRent.add(apartmentRent.multiply(BigDecimal.valueOf(ApartmentRentType.DAY.getOffset())));
						break;
					case NATURAL_MONTH:
						monthRent = monthRent.add(apartmentRent);
						break;
					case NATURAL_QUARTER:
						monthRent = monthRent.add(apartmentRent.divide(BigDecimal.valueOf(ApartmentRentType.NATURAL_QUARTER.getOffset()), 2, BigDecimal.ROUND_HALF_UP));
						break;
					case NATURAL_YEAR:
						monthRent = monthRent.add(apartmentRent.divide(BigDecimal.valueOf(ApartmentRentType.NATURAL_YEAR.getOffset()), 2, BigDecimal.ROUND_HALF_UP));
						break;
					case DAY_SQUARE_METRE:
						monthRent = monthRent.add(apartmentRent.multiply(chargeArea).multiply(BigDecimal.valueOf(ApartmentRentType.DAY_SQUARE_METRE.getOffset())));
						break;
					case NATURAL_MONTH_SQUARE_METRE:
						monthRent = monthRent.add(apartmentRent.multiply(BigDecimal.valueOf(address.getChargeArea())));
						break;
					case NATURAL_QUARTER_SQUARE_METRE:
						monthRent = monthRent.add(apartmentRent.multiply(chargeArea).divide(BigDecimal.valueOf(ApartmentRentType.NATURAL_QUARTER_SQUARE_METRE.getOffset()), 2, BigDecimal.ROUND_HALF_UP));
						break;
					case NATURAL_YEAR_SQUARE_METRE:
						monthRent = monthRent.add(apartmentRent.multiply(chargeArea).divide(BigDecimal.valueOf(ApartmentRentType.NATURAL_YEAR_SQUARE_METRE.getOffset()), 2, BigDecimal.ROUND_HALF_UP));
						break;
					}
				} else {
					LOGGER.error("only waiting for launch contract can launch!");
					throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE,
							ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY,
							"contract status is not waiting for launch!");
				}
			}
		}

		return monthRent;
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
