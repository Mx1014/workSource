package com.everhomes.flow;

public interface FlowScriptFire {
	void fireAction(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;
}
