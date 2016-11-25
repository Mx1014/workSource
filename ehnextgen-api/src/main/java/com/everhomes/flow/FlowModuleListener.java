package com.everhomes.flow;

public interface FlowModuleListener {
	FlowModuleInfo initModule();
	public void onFlowCaseStart(FlowCaseState ctx);
}
