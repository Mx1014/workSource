package com.everhomes.investmentAd;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;

public class InvestmentAdFLowCaseListener implements FlowModuleListener{

	@Autowired
    private FlowService flowService;
	
	@Override
	public FlowModuleInfo initModule() {
	    FlowModuleInfo module = new FlowModuleInfo();
        FlowModuleDTO moduleDTO = flowService.getModuleById(FlowConstants.BUSINESS_INVATION_MODULE);
        if (moduleDTO != null) {
            module.setModuleName(moduleDTO.getDisplayName());
            module.setModuleId(FlowConstants.BUSINESS_INVATION_MODULE);
            return module;
        }
        return null;
	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

}
