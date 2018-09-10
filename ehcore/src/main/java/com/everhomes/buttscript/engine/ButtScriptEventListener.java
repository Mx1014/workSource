package com.everhomes.buttscript.engine;

import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityRoster;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.SystemEvent;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.customer.*;
import com.everhomes.entity.EntityType;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.customer.PotentialCustomerType;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huangliangming  2018/9/10
 */
@Component
public class ButtScriptEventListener implements LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {


    private static final Logger LOGGER = LoggerFactory.getLogger(PotentialCustomerListener.class);

    @Override
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        try {
            ExecutorUtil.submit(() -> {
                LOGGER.info("potentialCustomerListener start run.....");
                LocalEvent localEvent = (LocalEvent) args;
            });
        } catch (Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
        }
        return Action.none;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            LOGGER.info("potential customer listener start ...equipmentIp:{}");
            LocalEventBus.subscribe(SystemEvent.ACTIVITY_ACTIVITY_ROSTER_CREATE.dft(), this);
        }
    }
}