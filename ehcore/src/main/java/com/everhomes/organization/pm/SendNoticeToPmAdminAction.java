package com.everhomes.organization.pm;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.organization.pm.SendNoticeToPmAdminCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 给管理员发送消息
 * Created by xq.tian on 2016/11/23.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendNoticeToPmAdminAction implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(SendNoticeToPmAdminAction.class);

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private UserProvider userProvider;

    private Long operateTime;// 创建发送任务的时间
    private SendNoticeToPmAdminCommand cmd;
    private Long userId;
    private String schema;

    @Override
    public void run() {
        User user = userProvider.findUserById(userId);

        UserContext.setCurrentNamespaceId(user.getNamespaceId());
        UserContext.setCurrentUser(user);
        UserContext.current().setScheme(schema);

        LOGGER.info("Push message to pm admin start");
        long start = System.currentTimeMillis();
        propertyMgrService.sendNoticeToPmAdmin(cmd, new Timestamp(operateTime));
        long end = System.currentTimeMillis();
        LOGGER.info("Push message to pm admin end, time = {} seconds", (end - start) / 1000);

        UserContext.setCurrentNamespaceId(null);
        UserContext.setCurrentUser(null);
        UserContext.current().setScheme(null);
    }

    public SendNoticeToPmAdminAction(String cmd, String operateTime, String userId, String schema) {
        this.operateTime = Long.valueOf(operateTime);
        this.cmd = JSON.parseObject(cmd, SendNoticeToPmAdminCommand.class);
        this.userId = Long.valueOf(userId);
        this.schema = schema;
    }
}
