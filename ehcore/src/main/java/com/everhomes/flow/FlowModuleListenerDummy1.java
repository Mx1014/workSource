package com.everhomes.flow;

import org.springframework.stereotype.Component;

@Component
public class FlowModuleListenerDummy1 implements FlowModuleListener {

	@Override
	public FlowModuleInfo initModule() {
		FlowModuleInfo module = new FlowModuleInfo();
		module.setModuleName("dummy-module1");
		module.setModuleId(1l);
		return module;
	}

	@Override
	public void onFlowCaseStart(FlowCaseContext ctx) {
		// TODO Auto-generated method stub
		
	}

}
