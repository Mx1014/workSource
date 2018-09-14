package com.everhomes.buttscript.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.SystemEvent;
import com.everhomes.customer.PotentialCustomerListener;



/**
 * Created by huangliangming  2018/9/10
 * 仅用来测试,目前已丢弃该种方式来监听事件,
 * 从kafka中监听事件,参考SystemEventButtListener
 */
@Component
public class ButtScriptEventListener implements LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {


    private static final Logger LOGGER = LoggerFactory.getLogger(PotentialCustomerListener.class);

    @Autowired
    private ButtScriptSchedulerMain buttScriptSchedulerMain ;

    @Override
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
                /*LOGGER.info("buttScriptEventListener start run.....");
                LocalEvent localEvent = (LocalEvent) args;
                System.out.println("事件监听到了");
                buttScriptSchedulerMain.doButtScriptMethod(localEvent);*/
        return Action.none;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        /*if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            LOGGER.info("buttScriptEventr listener start ...");
            LocalEventBus.subscribe("forum.post_like.1.0.0", this);
        }*/
    }


}