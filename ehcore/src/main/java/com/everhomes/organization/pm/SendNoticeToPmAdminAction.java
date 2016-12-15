package com.everhomes.organization.pm;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.organization.pm.SendNoticeToPmAdminCommand;
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

    private Long operateTime;// 创建发送任务的时间
    private SendNoticeToPmAdminCommand cmd;

    @Override
    public void run() {
        LOGGER.info("Push message to pm admin start");
        long start = System.currentTimeMillis();
        propertyMgrService.sendNoticeToPmAdmin(cmd, new Timestamp(operateTime));
        long end = System.currentTimeMillis();
        LOGGER.info("Push message to pm admin end, time = {} seconds", (end - start) / 1000);
    }

    public SendNoticeToPmAdminAction(String cmd, String operateTime) {
        this.operateTime = Long.valueOf(operateTime);
        this.cmd = JSON.parseObject(cmd, SendNoticeToPmAdminCommand.class);
    }
}
