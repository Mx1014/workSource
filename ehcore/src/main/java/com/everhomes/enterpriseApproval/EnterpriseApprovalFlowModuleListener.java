package com.everhomes.enterpriseApproval;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowUserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnterpriseApprovalFlowModuleListener implements FlowModuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalFlowModuleListener.class);

    @Autowired
    ServiceModuleProvider serviceModuleProvider;

    @Autowired
    GeneralApprovalProvider generalApprovalProvider;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(EnterpriseApprovalController.MODULE_ID);
        moduleInfo.setModuleId(module.getId());
        moduleInfo.setModuleName(module.getName());
        //  (启用工作流中的表单设置)
        moduleInfo.addMeta(FlowModuleInfo.META_KEY_FORM_FLAG, String.valueOf(TrueOrFalseFlag.TRUE.getCode()));
        return null;
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx){
        FlowCase flowCase = ctx.getRootState().getFlowCase();
        LOGGER.debug("审批被驳回,handler 执行 onFlowCaseAbsorted  userType : " + ctx.getCurrentEvent().getUserType());
        EnterpriseApprovalHandler handler = getGeneralApprovalHandler(flowCase.getReferId());
        handler.onFlowCaseAbsorted(ctx);
    }

    private EnterpriseApprovalHandler getGeneralApprovalHandler(Long referId) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(referId);
        if(ga!=null){
            EnterpriseApprovalHandler handler = PlatformContext.getComponent(EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX
                    + ga.getApprovalAttribute());
            if (handler != null) {
                return handler;
            }
        }
        return PlatformContext.getComponent(GeneralApprovalDefaultHandler.GENERAL_APPROVAL_DEFAULT_HANDLER_NAME);

//        return getGeneralApprovalHandler(ga.getApprovalAttribute());
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }
}
