// @formatter:off
package com.everhomes.techpark.expansion;

import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.techpark.expansion.ApplyEntrySourceType;
import com.everhomes.rest.techpark.expansion.ExpansionConst;
import com.everhomes.rest.techpark.expansion.ExpansionLocalStringCode;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2016/12/20.
 */
@Component
public class EnterpriseApplyEntryFlowListener implements FlowModuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApplyEntryFlowListener.class);

    @Autowired
    private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private CommunityProvider communityProvider;

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
        // 不用做什么
        /*ApplyEntryStatus status = null;
        switch (ctx.getStepType()) {
            case APPROVE_STEP:
                status = ApplyEntryStatus.RESIDED_IN;
                break;
            case REJECT_STEP:
                status = ApplyEntryStatus.INVALID;
                break;
            case ABSORT_STEP:
                status = ApplyEntryStatus.INVALID;
                break;
        }
        if (status != null) {
            enterpriseApplyEntryProvider.updateApplyEntryStatus(ctx.getFlowCase().getReferId(), status.getCode());
        }*/
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
            String locale = UserContext.current().getUser().getLocale();
            Map<String, Object> map = new HashMap<>();

            map.put("applyUserName", defaultIfNull(applyEntry.getApplyUserName(), ""));
            map.put("contactPhone", defaultIfNull(applyEntry.getApplyContact(), ""));
            map.put("enterpriseName", defaultIfNull(applyEntry.getEnterpriseName(), ""));
            String applyType = localeStringService.getLocalizedString(ExpansionLocalStringCode.SCOPE_APPLY_TYPE,
                    applyEntry.getApplyType() + "", locale, "");
            map.put("applyType", defaultIfNull(applyType, ""));
            map.put("areaSize", defaultIfNull(applyEntry.getAreaSize(), ""));
            this.processSourceName(applyEntry);
            map.put("sourceType", defaultIfNull(applyEntry.getSourceName(), ""));
            map.put("description", defaultIfNull(applyEntry.getDescription(), ""));

            String jsonStr = localeTemplateService.getLocaleTemplateString(ExpansionLocalStringCode.SCOPE, ExpansionLocalStringCode.FLOW_DETAIL_CONTENT_CODE, locale, map, "[]");
            return (FlowCaseEntityList) StringHelper.fromJsonString(jsonStr, FlowCaseEntityList.class);
        } else {
            LOGGER.warn("Not found EhEnterpriseOpRequests instance for flowCase: {}", StringHelper.toJsonString(flowCase));
        }
        return new ArrayList<>();
    }

    private Object defaultIfNull(Object obj, Object defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    private void processSourceName(EnterpriseOpRequest applyEntry) {
        applyEntry.setSourceName("");
        if(ApplyEntrySourceType.BUILDING.getCode().equals(applyEntry.getSourceType())){
            Building building = communityProvider.findBuildingById(applyEntry.getSourceId());
            if(null != building)
                applyEntry.setSourceName(building.getName());
        } else if (ApplyEntrySourceType.FOR_RENT.getCode().equals(applyEntry.getSourceType()) ||
                ApplyEntrySourceType.OFFICE_CUBICLE.getCode().equals(applyEntry.getSourceType())) {
            LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(applyEntry.getSourceId());
            if (null != leasePromotion)
                applyEntry.setSourceName(leasePromotion.getSubject());
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
}
