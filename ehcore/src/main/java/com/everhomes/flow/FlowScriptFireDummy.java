package com.everhomes.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowScriptFireDummy implements FlowScriptFire {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowScriptFireDummy.class);
	
	@Override
	public void fireAction(FlowCaseState ctx, FlowGraphEvent event)
			throws FlowStepErrorException {
		LOGGER.info("dumpy script build fired");
	}

}
