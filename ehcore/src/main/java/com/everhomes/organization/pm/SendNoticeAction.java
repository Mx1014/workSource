// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.pushmessagelog.PushMessageLogService;
import com.everhomes.rest.organization.pm.SendNoticeCommand;
import com.everhomes.rest.pushmessagelog.PushStatusCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendNoticeAction implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendNoticeAction.class);

    private String cmd;
    private Long operatorUid;
    private String schema;
    private Integer namespaceId;

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
   	private PushMessageLogService pushMessageLogService;

    @Override
    public void run() {
        //一键推送
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
        }
        
        try{
        	
        	propertyMgrService.pushMessage(command, operator);
        	
        }catch(Exception e){
        	//在有异常的情况下也要更新推送记录状态为完成
            pushMessageLogService.updatePushStatus(command.getLogId(), PushStatusCode.FINISH.getCode());
            throw e ;
        }
        
        //更新推送记录状态为完成
        pushMessageLogService.updatePushStatus(command.getLogId(), PushStatusCode.FINISH.getCode());
        LOGGER.debug("End scheduling a push to push....");

        UserContext.setCurrentNamespaceId(null);
        UserContext.setCurrentUser(null);
        UserContext.current().setScheme(null);
    }

    public SendNoticeAction(String cmd, String operatorUid, String schema, Integer namespaceId){
        this.cmd = cmd;
        this.operatorUid = Long.valueOf(operatorUid);
        this.schema = schema;
        this.namespaceId = namespaceId;
    }
}
