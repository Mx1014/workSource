package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowUserType;

import java.util.List;

public class EnterpriseApprovalFlowModuleListener implements FlowModuleListener {
    @Override
    public FlowModuleInfo initModule() {
        return null;
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
