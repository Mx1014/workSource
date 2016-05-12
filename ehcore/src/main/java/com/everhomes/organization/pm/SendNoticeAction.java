package com.everhomes.organization.pm;

import com.everhomes.rest.organization.pm.PropCommunityBuildAddessCommand;

public class SendNoticeAction implements Runnable {

	private PropCommunityBuildAddessCommand cmd;
	
	private PropertyMgrService propertyMgrService;
	
	@Override
	public void run() {
		//一键推送
		propertyMgrService.pushMessage(cmd);
	}

	public SendNoticeAction(PropCommunityBuildAddessCommand cmd, PropertyMgrService propertyMgrService){
		this.cmd = cmd;
		this.propertyMgrService = propertyMgrService;
	}
}
