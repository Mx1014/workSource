package com.everhomes.organization.pm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.rest.organization.pm.PropCommunityBuildAddessCommand;

public class SendNoticeAction implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SendNoticeAction.class);

	private PropCommunityBuildAddessCommand cmd;
	
	private PropertyMgrService propertyMgrService;
	
	@Override
	public void run() {
		//一键推送
		LOGGER.debug("Start scheduling a push to push....");
		propertyMgrService.pushMessage(cmd);
		LOGGER.debug("End scheduling a push to push....");
	}

	public SendNoticeAction(PropCommunityBuildAddessCommand cmd, PropertyMgrService propertyMgrService){
		this.cmd = cmd;
		this.propertyMgrService = propertyMgrService;
	}
}
