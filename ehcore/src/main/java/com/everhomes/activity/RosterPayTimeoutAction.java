package com.everhomes.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowStatusType;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RosterPayTimeoutAction implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(RosterPayTimeoutAction.class);
	
	@Autowired
	RosterPayTimeoutService rosterPayTimeoutService;
	
	private Long rosterId;

	@Override
	public void run() {
		rosterPayTimeoutService.cancelTimeoutOrder(rosterId);
	}

	public RosterPayTimeoutAction(final String rosterId) {
		this.rosterId = Long.parseLong(rosterId);
	}
}
