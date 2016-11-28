package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowGraphScriptAction extends FlowGraphAction {
	@Autowired
	private FlowScriptProvider flowScriptProvider;
	
	@Override
	public void fireAction(FlowCaseState ctx, FlowGraphEvent event)
			throws FlowStepErrorException {
		FlowScript flowScript = flowScriptProvider.getFlowScriptById(this.getFlowAction().getScriptId());
		if(flowScript != null) {
			//TODO SpringWorker
		}
	}

}
