package com.everhomes.flow;

public interface FlowListenerManager {

	int getListenerSize();

	void onFlowCaseStart(FlowCaseContext ctx);
}
