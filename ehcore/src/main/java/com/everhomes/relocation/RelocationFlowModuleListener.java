// @formatter:off
package com.everhomes.relocation;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.relocation.RelocationRequestStatus;
import com.everhomes.rest.relocation.RelocationTemplateCode;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.techpark.expansion.*;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RelocationFlowModuleListener implements FlowModuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelocationFlowModuleListener.class);

    @Autowired
    private RelocationProvider relocationProvider;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private EnterpriseApplyEntryService enterpriseApplyEntryService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private AddressProvider addressProvider;
    @Autowired
    private UserProvider userProvider;

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
            RelocationRequest request = relocationProvider.findRelocationRequestById(flowCase.getReferId());
            if (null != request) {
                request.setStatus(convertStatus(status));
                relocationProvider.updateRelocationRequest(request);
            }
        }
    }

    private Byte convertStatus(Byte flowCaseStatus) {
        FlowCaseStatus status = FlowCaseStatus.fromCode(flowCaseStatus);
        if (null != status) {
            switch (status) {
                case PROCESS: return RelocationRequestStatus.PROCESSING.getCode();
                case ABSORTED: return RelocationRequestStatus.CANCELED.getCode();
                case FINISHED: return RelocationRequestStatus.COMPLETED.getCode();
                default: return RelocationRequestStatus.PROCESSING.getCode();
            }
        }
        return null;
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return flowCase.getContent();
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        RelocationRequest request = relocationProvider.findRelocationRequestById(flowCase.getReferId());
        if (request != null) {
            String locale = UserContext.current().getUser().getLocale();

            String defaultValue = localeStringService.getLocalizedString(ApplyEntryErrorCodes.SCOPE, String.valueOf(ApplyEntryErrorCodes.WU), locale, "");

            EnterpriseApplyEntryDTO dto = ConvertHelper.convert(applyEntry, EnterpriseApplyEntryDTO.class);

            if (null != applyEntry.getAddressId()) {
                Address address = addressProvider.findAddressById(applyEntry.getAddressId());
                if (null != address) {
                    dto.setApartmentName(address.getApartmentName());
                    dto.setBuildingName(address.getBuildingName());
                }
            }
            flowCase.setCustomObject(JSONObject.toJSONString(dto));

            Map<String, Object> map = new HashMap<>();

            String buildingName = processBuildingName(applyEntry);

            map.put("applyBuilding", defaultIfNull(buildingName, ""));
            map.put("applyUserName", defaultIfNull(applyEntry.getApplyUserName(), ""));

            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(applyEntry.getApplyUserId(), IdentifierType.MOBILE.getCode());

            map.put("contactPhone", defaultIfNull(userIdentifier.getIdentifierToken(), ""));
            map.put("enterpriseName", defaultIfNull(applyEntry.getEnterpriseName(), ""));

            map.put("sourceType", defaultIfNull(enterpriseApplyEntryService.getSourceTypeName(applyEntry.getSourceType()), ""));

            map.put("description", StringUtils.isBlank(applyEntry.getDescription()) ? defaultValue : applyEntry.getDescription());

            String jsonStr = localeTemplateService.getLocaleTemplateString(ApplyEntryErrorCodes.SCOPE, ApplyEntryErrorCodes.FLOW_DETAIL_CONTENT_CODE, locale, map, "[]");

            FlowCaseEntityList result = (FlowCaseEntityList) StringHelper.fromJsonString(jsonStr, FlowCaseEntityList.class);
            return result;
        } else {
            LOGGER.warn("RelocationRequest ot found flowCase: {}", StringHelper.toJsonString(flowCase));
        }
        return new ArrayList<>();
    }

    private String defaultIfNull(String obj, String defaultValue) {
        return obj != null ? obj : defaultValue;
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
        FlowModuleDTO module = flowService.getModuleById(FlowConstants.RELOCATION_MODULE);
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
                                        List<Tuple<String, Object>> variables) {}

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId) {
        List<FlowServiceTypeDTO> result = new ArrayList<>();
        FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
        dto.setNamespaceId(namespaceId);

        String locale = UserContext.current().getUser().getLocale();

        String serviceName = localeStringService.getLocalizedString(RelocationTemplateCode.SCOPE,
                String.valueOf(RelocationTemplateCode.SERVICE_TYPE_NAME), locale, "");

        dto.setServiceName(serviceName);
        result.add(dto);
        return result;
    }
}
