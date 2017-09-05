// @formatter:off
package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.techpark.expansion.ApplyEntryApplyType;
import com.everhomes.rest.techpark.expansion.ApplyEntrySourceType;
import com.everhomes.rest.techpark.expansion.ExpansionConst;
import com.everhomes.rest.techpark.expansion.ApplyEntryErrorCodes;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.Tables;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.yellowPage.YellowPage;
import com.everhomes.yellowPage.YellowPageProvider;

import org.apache.commons.lang.StringUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EnterpriseApplyEntryFlowListener implements FlowModuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApplyEntryFlowListener.class);

    @Autowired
    private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private EnterpriseApplyEntryService enterpriseApplyEntryService;
    @Autowired
    private YellowPageProvider yellowPageProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private AddressProvider addressProvider;
    @Autowired
    private BuildingProvider buildingProvider;
    @Autowired
    private ContractProvider contractProvider;
    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;
    @Autowired
    private SmsProvider smsProvider;
    @Autowired
    private FlowEventLogProvider flowEventLogProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private EnterpriseApplyBuildingProvider enterpriseApplyBuildingProvider;
    @Autowired
    private EnterpriseOpRequestBuildingProvider enterpriseOpRequestBuildingProvider;
    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        // 更新状态
        FlowCase flowCase = ctx.getFlowCase();
        Byte status = flowCase.getStatus();

        if (status != null) {
            enterpriseApplyEntryProvider.updateApplyEntryStatus(ctx.getFlowCase().getReferId(), status);
        }
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase) {
        return flowCase.getContent();
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        EnterpriseOpRequest applyEntry = enterpriseApplyEntryProvider.getApplyEntryById(flowCase.getReferId());
        if (applyEntry != null) {
            EnterpriseApplyEntryDTO dto = ConvertHelper.convert(applyEntry, EnterpriseApplyEntryDTO.class);

            if (null != applyEntry.getAddressId()){
                Address address = addressProvider.findAddressById(applyEntry.getAddressId());
                if (null != address){
                    dto.setApartmentName(address.getApartmentName());
                    dto.setBuildingName(address.getBuildingName());
                }
            }
            flowCase.setCustomObject(JSONObject.toJSONString(dto));

            String locale = UserContext.current().getUser().getLocale();
            Map<String, Object> map = new HashMap<>();

            String buildingName = processBuildingName(applyEntry);

            map.put("applyBuilding", defaultIfNull(buildingName, ""));
            map.put("applyUserName", defaultIfNull(applyEntry.getApplyUserName(), ""));

            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(applyEntry.getApplyUserId(), IdentifierType.MOBILE.getCode());

            map.put("contactPhone", defaultIfNull(userIdentifier.getIdentifierToken(), ""));
            map.put("enterpriseName", defaultIfNull(applyEntry.getEnterpriseName(), ""));
//            String applyType = localeStringService.getLocalizedString(ExpansionLocalStringCode.SCOPE_APPLY_TYPE,
//                    applyEntry.getApplyType() + "", locale, "");
            ApplyEntrySourceType applyEntrySourceType = ApplyEntrySourceType.fromType(applyEntry.getSourceType());
            String sourceType = null != applyEntrySourceType? applyEntrySourceType.getDescription() : "";

            GetLeasePromotionConfigCommand cmd = new GetLeasePromotionConfigCommand();
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
            LeasePromotionConfigDTO config = enterpriseApplyEntryService.getLeasePromotionConfig(cmd);
            byte i = -1;
            if (ApplyEntrySourceType.BUILDING == applyEntrySourceType) {
                i = LeasePromotionOrder.PARK_INTRODUCE.getCode();
            }else if (ApplyEntrySourceType.FOR_RENT == applyEntrySourceType) {
                i = LeasePromotionOrder.LEASE_PROMOTION.getCode();
            }
            if (null != config.getDisplayNames() ) {
                for (Integer k: config.getDisplayOrders()) {
                    if (k.byteValue() ==i) {
                        sourceType =config.getDisplayNames().get(k - 1);
                    }
                }
            }

            map.put("sourceType", defaultIfNull(sourceType, ""));

            map.put("description", defaultIfNull(applyEntry.getDescription(), ""));
            
            String jsonStr;

            jsonStr = localeTemplateService.getLocaleTemplateString(ApplyEntryErrorCodes.SCOPE, ApplyEntryErrorCodes.FLOW_DETAIL_CONTENT_CODE, locale, map, "[]");

            GetGeneralFormValuesCommand cmd2 = new GetGeneralFormValuesCommand();
            cmd2.setSourceType(EntityType.ENTERPRISE_OP_REQUEST.getCode());
            cmd2.setSourceId(dto.getId());
            List<FlowCaseEntity> formEntities = generalFormService.getGeneralFormFlowEntities(cmd2);

            formEntities.forEach(r -> {
                if (StringUtils.isBlank(r.getValue())) {
                    r.setValue("无");
                }
            });

            FlowCaseEntityList result = (FlowCaseEntityList) StringHelper.fromJsonString(jsonStr, FlowCaseEntityList.class);
            result.addAll(result.size() - 1, formEntities);
            return result;
        } else {
            LOGGER.warn("Not found EhEnterpriseOpRequests instance for flowCase: {}", StringHelper.toJsonString(flowCase));
        }
        return new ArrayList<>();
    }

    private Object defaultIfNull(Object obj, Object defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    private String processBuildingName(EnterpriseOpRequest applyEntry) {
        String buildingName = "";
        if(ApplyEntryApplyType.fromType(applyEntry.getApplyType()).equals(ApplyEntryApplyType.RENEW)){
			//续租的
            if (null != applyEntry.getContractId()) {
                Contract contract = contractProvider.findContractById(applyEntry.getContractId());

                List<BuildingApartmentDTO> buildings = contractBuildingMappingProvider.listBuildingsByContractNumber(UserContext.getCurrentNamespaceId(),
                        contract.getContractNumber());
                Set<String> buildingNames = new HashSet<>();
                buildings.forEach(b -> buildingNames.add(b.getBuildingName()));

                StringBuilder sb = new StringBuilder();
                int n = 1;
                for (String name: buildingNames) {

                    if (n == buildingNames.size()) {
                        sb.append(name);
                    }else {
                        sb.append(name).append(",");
                    }
                    n++;
                }

                buildingName = sb.toString();
            }else {
                buildingName = getBuildingName(applyEntry.getId());
            }

		}else if(ApplyEntrySourceType.BUILDING.getCode().equals(applyEntry.getSourceType())){
			//园区介绍处的申请，申请来源=楼栋名称 园区介绍处的申请，楼栋=楼栋名称
            LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(applyEntry.getSourceId());
			if(null != leaseBuilding){
                buildingName = leaseBuilding.getName();
            }
		}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(applyEntry.getSourceType())){

            LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(applyEntry.getSourceId());

            if (leasePromotion.getBuildingId() == EnterpriseApplyEntryService.OTHER_BUILDING_ID) {

                buildingName = leasePromotion.getBuildingName();
            }else {
                LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(leasePromotion.getBuildingId());
                if (null != leaseBuilding) {
                    buildingName = leaseBuilding.getName();
                }
            }

            Address address = addressProvider.findAddressById(applyEntry.getAddressId());

            if (null != address) {
                buildingName = address.getBuildingName() + " " + address.getApartmentName();
            }
		}else if (ApplyEntrySourceType.MARKET_ZONE.getCode().equals(applyEntry.getSourceType())){
			//创客入驻处的申请，申请来源=“创客申请” 创客入驻处的申请，楼栋=创客空间所在的楼栋
			YellowPage yellowPage = yellowPageProvider.getYellowPageById(applyEntry.getSourceId());
			if(null != yellowPage){
                if (null != yellowPage.getBuildingId()) {
                    LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingByBuildingId(yellowPage.getBuildingId());
                    if(null != leaseBuilding){
                        buildingName = leaseBuilding.getName();
                    }
                }
			}
		}

        return buildingName;
    }

    private String getBuildingName(Long applyEntryId) {
        EnterpriseOpRequestBuilding enterpriseOpRequestBuilding = getEnterpriseOpRequestBuildingByRequestId(applyEntryId);
        if (null != enterpriseOpRequestBuilding) {
            LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(enterpriseOpRequestBuilding.getBuildingId());
            if(null != leaseBuilding){
                return leaseBuilding.getName();
            }
        }
        return "";
    }

    private EnterpriseOpRequestBuilding getEnterpriseOpRequestBuildingByRequestId(Long requestId) {
        List<EnterpriseOpRequestBuilding> opRequestBuildings = this.enterpriseOpRequestBuildingProvider.queryEnterpriseOpRequestBuildings(
                new ListingQueryBuilderCallback() {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.ENTERPRISE_OP_REQUESTS_ID.eq(requestId));
                        return query;
                    }
                });
        if (opRequestBuildings.isEmpty()) {
            return null;
        }else {
            return opRequestBuildings.get(0);
        }
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleDTO module = flowService.getModuleById(ExpansionConst.MODULE_ID);
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        moduleInfo.setModuleId(module.getModuleId());
        moduleInfo.setModuleName(module.getModuleName());
        return moduleInfo;
    }

    @Override
    public void onFlowCreating(Flow flow) {

    }

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {

        FlowCase flowCase = ctx.getFlowCase();
        EnterpriseOpRequest applyEntry = enterpriseApplyEntryProvider.getApplyEntryById(flowCase.getReferId());
        String applyUserName = applyEntry.getApplyUserName();
        String applyContact = applyEntry.getApplyContact();
        if (SmsTemplateCode.APPLY_ENTRY_PROCESSING_NODE_CODE == templateId) {

            smsProvider.addToTupleList(variables, "applyUserName", applyUserName);
            smsProvider.addToTupleList(variables, "applyContact", applyContact);

        }else if (SmsTemplateCode.APPLY_ENTRY_PROCESSING_BUTTON_APPROVE_CODE == templateId){
            //TODO: 给被分配的人发短信

            FlowEventLog flowEventLog = null;
            List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(ctx.getNextNode().getFlowNode().getId()
                    , ctx.getFlowCase().getId()
                    , ctx.getFlowCase().getStepCount()); ////stepCount 不加 1 的原因是，目标节点处理人是当前 stepCount 计算的 node_enter 的值
            if(logs != null && logs.size() > 0) {
                for(FlowEventLog log : logs) {
                    if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
                        flowEventLog = log;
                    }
                }
            }

            if (null != flowEventLog) {
                if (null != flowEventLog.getFlowSelectionId()) {
                    User entityUser = userProvider.findUserById(flowEventLog.getFlowUserId());
                    UserIdentifier entityIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(entityUser.getId(), IdentifierType.MOBILE.getCode());
                    smsProvider.addToTupleList(variables, "operatorName", entityUser.getNickName());
                    smsProvider.addToTupleList(variables, "operatorContact", entityIdentifier.getIdentifierToken());
                }
            }

        }else if (SmsTemplateCode.APPLY_ENTRY_PROCESSING_BUTTON_ABSORT_CODE == templateId){
            //
        }else if (SmsTemplateCode.APPLY_ENTRY_PROCESSING_BUTTON_REMINDER_CODE == templateId){
            smsProvider.addToTupleList(variables, "applyUserName", applyUserName);
            smsProvider.addToTupleList(variables, "applyContact", applyContact);

        }else if (SmsTemplateCode.APPLY_ENTRY_COMPLETED_CODE == templateId){

        }
		
	}
}
