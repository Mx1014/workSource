package com.everhomes.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowStatusType;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowTimeoutAction implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(FlowTimeoutAction.class);
	
	@Autowired
	FlowTimeoutProvider flowTimeoutProvider;
	
	@Autowired
	FlowTimeoutService flowTimeoutService;
	
	private Long timeoutId;

	@Override
	public void run() {
		if(flowTimeoutProvider.deleteIfValid(timeoutId)) {
			//delete ok, means we take it's owner
			FlowTimeout ft = flowTimeoutProvider.getFlowTimeoutById(timeoutId);
			flowTimeoutService.processTimeout(ft);
		}
	}
	
	public FlowTimeoutAction(final String timeoutId) {
		this.timeoutId = Long.parseLong(timeoutId);
	}
}
