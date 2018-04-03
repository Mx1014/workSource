package com.everhomes.bus;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.user.UserContext;

/**
 * Created by xq.tian on 2017/12/5.
 */
public class LocalEventBus {

    private volatile static LocalEventBus localEventBus;

    private LocalBus localBus;

    private LocalEventBus() {
        localBus = PlatformContext.getComponent(LocalBusProvider.class);
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
    }

    public static void publish(LocalEventBuilder builder) {
        LocalEvent event = new LocalEvent();
        builder.build(event);
        publish(event);
    }

    public static void publish(String eventName, String entityType, Long entityId) {
        publish(new LocalEvent(eventName, entityType, entityId));
    }

    public static void publishWithUserContext(LocalEvent event) {
        getLocalEventBus().populateUserContext(event);
        publish(event);
    }

    public static void publishWithUserContext(LocalEventBuilder builder) {
        LocalEvent event = new LocalEvent();
        builder.build(event);
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

    public interface LocalEventBuilder {
        void build(LocalEvent event);
    }
}