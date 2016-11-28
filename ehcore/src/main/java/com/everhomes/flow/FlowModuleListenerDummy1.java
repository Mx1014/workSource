package com.everhomes.flow;

import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowCaseEntity;

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
	public void onFlowCaseStart(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		// TODO Auto-generated method stub
		return null;
	}

}
