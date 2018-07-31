// @formatter:off
package com.everhomes.organization.pm;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.pushmessagelog.PushMessageLogService;
import com.everhomes.rest.organization.pm.SendNoticeCommand;
import com.everhomes.rest.pushmessagelog.PushStatusCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;

@Component
public class SendNoticeNewAction extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendNoticeNewAction.class);
    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
   	private PushMessageLogService pushMessageLogService;
    
    
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			if(context==null || context.getMergedJobDataMap()==null){
				LOGGER.debug("context or paramMap is null !");
				return;
			}
			 String cmd;
		     Long operatorUid;
		     String schema;
		     Integer namespaceId;
		    
		     JobDataMap paramMap = context.getMergedJobDataMap();
		   
			 cmd = paramMap.getString("cmd");
			 namespaceId = paramMap.getIntValue("namespaceId");
			 operatorUid = 	paramMap.getLong("operatorUid"); 
			 schema = paramMap.getString("schema");
			LOGGER.debug("Start scheduling a push to push...." + cmd);

	        User operator = userProvider.findUserById(operatorUid);
	        operator.setNamespaceId(namespaceId);
	        UserContext.setCurrentUser(operator);
	        UserContext.current().setScheme(schema);
	        UserContext.setCurrentNamespaceId(operator.getNamespaceId());

	        SendNoticeCommand command = (SendNoticeCommand) StringHelper.fromJsonString(cmd, SendNoticeCommand.class);
	        if(command !=null){
	        	//更新推送记录状态
	            pushMessageLogService.updatePushStatus(command.getLogId(), PushStatusCode.PUSHING.getCode());
	            LOGGER.info("update pushMessageLog status pushing .");
	        }
	        
	        try{
	        	
	        	propertyMgrService.pushMessage(command, operator);
	        	
	        }catch(Exception e){
	        	//在有异常的情况下也要更新推送记录状态为完成
	            pushMessageLogService.updatePushStatus(command.getLogId(), PushStatusCode.FINISH.getCode());
	            LOGGER.info("update pushMessageLog status finish ,e={}",e);
	            throw e ;
	        }
	        
	        //更新推送记录状态为完成
	        pushMessageLogService.updatePushStatus(command.getLogId(), PushStatusCode.FINISH.getCode());
	        LOGGER.info("update pushMessageLog status finish .");
	        LOGGER.debug("End scheduling a push to push....");

	        UserContext.setCurrentNamespaceId(null);
	        UserContext.setCurrentUser(null);
	        UserContext.current().setScheme(null);
            
        } catch (Exception e) {
            LOGGER.error("ArchivesNotificationJob Failed!", e);
        }
		
	}

    
}
