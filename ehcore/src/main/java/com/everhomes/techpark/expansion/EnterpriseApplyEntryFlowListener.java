// @formatter:off
package com.everhomes.techpark.expansion;

import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.flow.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.techpark.expansion.ApplyEntryApplyType;
import com.everhomes.rest.techpark.expansion.ApplyEntrySourceType;
import com.everhomes.rest.techpark.expansion.EnterpriseOpRequestBuildingStatus;
import com.everhomes.rest.techpark.expansion.ExpansionConst;
import com.everhomes.rest.techpark.expansion.ExpansionLocalStringCode;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.yellowPage.YellowPage;
import com.everhomes.yellowPage.YellowPageProvider;

import org.jooq.Record;
import org.jooq.SelectQuery;
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
    private YellowPageProvider yellowPageProvider;
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
            
            String jsonStr = "";
            if(null != applyEntry.getAreaSize())
            	jsonStr = localeTemplateService.getLocaleTemplateString(ExpansionLocalStringCode.SCOPE, ExpansionLocalStringCode.FLOW_DETAIL_CONTENT_CODE, locale, map, "[]");
            else
            	jsonStr = localeTemplateService.getLocaleTemplateString(ExpansionLocalStringCode.SCOPE, ExpansionLocalStringCode.FLOW_DETAIL_CONTENT_NOAREA_CODE, locale, map, "[]");
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
        if(ApplyEntryApplyType.fromType(applyEntry.getApplyType()).equals(ApplyEntryApplyType.RENEW)){
			//续租的 
        	applyEntry.setSourceName("续租"); 
			 
		}else if(ApplyEntrySourceType.BUILDING.getCode().equals(applyEntry.getSourceType())){
			//园区介绍处的申请，申请来源=楼栋名称 园区介绍处的申请，楼栋=楼栋名称
			Building building = communityProvider.findBuildingById(applyEntry.getSourceId());
			if(null != building){
				applyEntry.setSourceName(building.getName());
			}
		}else if(ApplyEntrySourceType.FOR_RENT.getCode().equals(applyEntry.getSourceType())||
				ApplyEntrySourceType.OFFICE_CUBICLE.getCode().equals(applyEntry.getSourceType())){
			//虚位以待处的申请，申请来源=招租标题 虚位以待处的申请，楼栋=招租办公室所在楼栋
			LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(applyEntry.getSourceId());
			if(null != leasePromotion){
				applyEntry.setSourceName(leasePromotion.getSubject()); 
			}
		}else if (ApplyEntrySourceType.MARKET_ZONE.getCode().equals(applyEntry.getSourceType())){
			//创客入驻处的申请，申请来源=“创客申请” 创客入驻处的申请，楼栋=创客空间所在的楼栋
			YellowPage yellowPage = yellowPageProvider.getYellowPageById(applyEntry.getSourceId());
			if(null != yellowPage){
				applyEntry.setSourceName("创客申请"); 
			}
		}
//        if(ApplyEntrySourceType.BUILDING.getCode().equals(applyEntry.getSourceType())){
//            Building building = communityProvider.findBuildingById(applyEntry.getSourceId());
//            if(null != building)
//                applyEntry.setSourceName(building.getName());
//        } else if (ApplyEntrySourceType.FOR_RENT.getCode().equals(applyEntry.getSourceType()) ||
//                ApplyEntrySourceType.OFFICE_CUBICLE.getCode().equals(applyEntry.getSourceType())) {
//            LeasePromotion leasePromotion = enterpriseApplyEntryProvider.getLeasePromotionById(applyEntry.getSourceId());
//            if (null != leasePromotion)
//                applyEntry.setSourceName(leasePromotion.getSubject());
//        }
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
		// TODO Auto-generated method stub
		
	}
}
