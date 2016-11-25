package com.everhomes.flow;

import org.springframework.stereotype.Component;

@Component
public class FlowModuleListenerDummy2 implements FlowModuleListener {

	@Override
	public FlowModuleInfo initModule() {
		FlowModuleInfo module = new FlowModuleInfo();
		module.setModuleName("dummy-module2");
		module.setModuleId(2l);
		return module;
	}

	@Override
	public void onFlowCaseStart(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

}
