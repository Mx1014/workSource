package com.everhomes.bus;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.user.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.function.Consumer;

/**
 * Created by xq.tian on 2017/12/5.
 */
public class LocalEventBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalEventBus.class);

    private volatile static LocalEventBus localEventBus;

    private LocalBus localBus;
    private KafkaTemplate<String, String> kafkaTemplate;

    @SuppressWarnings("unchecked")
    private LocalEventBus() {
        localBus = PlatformContext.getComponent(LocalBusProvider.class);
        kafkaTemplate = PlatformContext.getComponent(KafkaTemplate.class);
    }

    private static LocalEventBus getLocalEventBus() {
        if (localEventBus == null) {
            synchronized (LocalEventBus.class) {
                if (localEventBus == null) {
                    localEventBus = new LocalEventBus();
                }
            }
        }
        return localEventBus;
    }

    public static void publish(LocalEvent event) {
        getLocalEventBus().populateEvent(event);
        getLocalEventBus().localBus.publish(getLocalEventBus(), event.getEventName(), event);

        if (event.getContext() != null && event.getContext().getUid() != null) {
            int partition = (int) (event.getContext().getUid()%100);
            getLocalEventBus().kafkaTemplate.send("system-event", partition, String.valueOf(partition), event.toString());
        } else {
            LOGGER.warn("Invalid event, can not send to kafka, event={}", event);
        }
    }

    public static void publish(Consumer<LocalEvent> builder) {
        LocalEvent event = new LocalEvent();
        builder.accept(event);
        publish(event);
    }

    public static void publish(String eventName, String entityType, Long entityId) {
        publish(new LocalEvent(eventName, entityType, entityId));
    }

    public static void publishWithUserContext(LocalEvent event) {
        getLocalEventBus().populateUserContext(event);
        publish(event);
    }

    public static void publishWithUserContext(Consumer<LocalEvent> builder) {
        LocalEvent event = new LocalEvent();
        builder.accept(event);
        publishWithUserContext(event);
    }

    public static void publishWithUserContext(String eventName, String entityType, Long entityId) {
        publishWithUserContext(new LocalEvent(eventName, entityType, entityId));
    }

    public static void subscribe(String eventName, LocalBusSubscriber subscriber) {
        getLocalEventBus().localBus.subscribe(eventName, subscriber);
    }

    private void populateEvent(LocalEvent event) {
        if (event.getCreateTime() == null) {
            event.setCreateTime(System.currentTimeMillis());
        }
    }

    private void populateUserContext(LocalEvent event) {
        LocalEventContext context = new LocalEventContext(null, UserContext.getCurrentNamespaceId(), UserContext.currentUserId());
        event.setContext(context);
    }
}