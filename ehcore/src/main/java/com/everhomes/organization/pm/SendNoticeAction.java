// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.rest.organization.pm.SendNoticeCommand;
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
        propertyMgrService.pushMessage(command, operator);

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
