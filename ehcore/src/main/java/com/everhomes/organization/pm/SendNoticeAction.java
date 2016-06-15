package com.everhomes.organization.pm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.rest.organization.pm.PropCommunityBuildAddessCommand;
import com.everhomes.user.User;
import com.everhomes.util.StringHelper;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendNoticeAction implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SendNoticeAction.class);

	private String cmd;
	
	private String user;
	
	@Autowired
	private PropertyMgrService propertyMgrService;
	
	@Override
	public void run() {
		//一键推送
		LOGGER.debug("Start scheduling a push to push...." + cmd);
		PropCommunityBuildAddessCommand command = (PropCommunityBuildAddessCommand) StringHelper.fromJsonString(cmd, PropCommunityBuildAddessCommand.class);
		User u = (User) StringHelper.fromJsonString(user, User.class);
		propertyMgrService.pushMessage(command, u);
		LOGGER.debug("End scheduling a push to push....");
	}

	public SendNoticeAction(String cmd, String user){
		this.cmd = cmd;
		this.user = user;
	}
}
