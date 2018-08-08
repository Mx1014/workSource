package com.everhomes.contentserver;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class MessageQueue {
    private static final MessageQueue instance = new MessageQueue();

    @Autowired
    private LocalBus localBus;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ContentServerMananger contentProvider;

    @Autowired(required = true)
    private LocalBusOneshotSubscriberBuilder localBusOneshotSubscriberBuilder;

    @Bean
    public static final MessageQueue getInstance() {
        return instance;
    }

    private MessageQueue() {
    }

    public void submitTask(MessageTask task) {
        task.setContentServerMannager(contentProvider);
        taskExecutor.execute(task);
    }

    public void publish(Object sender, String subId, Object content) {
        localBus.publish(sender, subId, content);
    }

    public void subscriber(String node, LocalBusOneshotSubscriber subscriber) {
        localBusOneshotSubscriberBuilder.build(node, subscriber).create();
    }
}
