package com.everhomes.pusher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.EnhancedApnsNotification;
import com.notnoop.apns.internal.Utilities;
import com.notnoop.exceptions.NetworkIOException;

public class PriorityQueuedApnsService implements ApnsService {

    private static final Logger logger = LoggerFactory.getLogger(PriorityQueuedApnsService.class);
    
    private ApnsService service;
    private PriorityBlockingQueue<PriorityApnsNotification> queue;
    private AtomicBoolean started = new AtomicBoolean(false);
    private AtomicInteger c = new AtomicInteger();

    public PriorityQueuedApnsService(ApnsService service) {
        this(service, null);
    }

    public PriorityQueuedApnsService(ApnsService service, final ThreadFactory tf) {
        this.service = service;
        this.queue = new PriorityBlockingQueue<PriorityApnsNotification>();
        this.threadFactory = tf == null ? Executors.defaultThreadFactory() : tf;
        this.thread = null;
    }

    public void push(PriorityApnsNotification msg) {
        if (!started.get()) {
            throw new IllegalStateException("service hasn't be started or was closed");
        }
        queue.add(msg);
    }

    private final ThreadFactory threadFactory;
    private Thread thread;
    private volatile boolean shouldContinue;

    public void start() {
        if (started.getAndSet(true)) {
            // I prefer if we throw a runtime IllegalStateException here,
            // but I want to maintain semantic backward compatibility.
            // So it is returning immediately here
            return;
        }

        //service.start(); the real service is already started
        shouldContinue = true;
        thread = threadFactory.newThread(new Runnable() {
            public void run() {
                while (shouldContinue) {
                    try {
                        PriorityApnsNotification msg = queue.take();
                        service.push(msg);
                        long sub = System.currentTimeMillis() - msg.getStartTick();
                        logger.info("Pushing message startTick=" + msg.getStartTick() + ", sub=" + sub + ", priority=" + msg.getPriority());
                    } catch (InterruptedException e) {
                        // ignore
                    } catch (NetworkIOException e) {
                        // ignore: failed connect...
                    } catch (Exception e) {
                        // weird if we reached here - something wrong is happening, but we shouldn't stop the service anyway!
                        logger.warn("Unexpected message caught... Shouldn't be here", e);
                    }
                }
            }
        });
        thread.start();
    }

    public void stop() {
        started.set(false);
        shouldContinue = false;
        thread.interrupt();
        service.stop();
    }
    
    public Map<String, Date> getInactiveDevices() throws NetworkIOException {
        return service.getInactiveDevices();
    }

    public void testConnection() throws NetworkIOException {
        service.testConnection();
    }

    @Override
    public ApnsNotification push(String deviceToken, String payload) throws NetworkIOException {
        PriorityApnsNotification notification =
                new PriorityApnsNotification(c.incrementAndGet(), EnhancedApnsNotification.MAXIMUM_EXPIRY, deviceToken, payload);
            push(notification);
            return notification;
    }

    @Override
    public EnhancedApnsNotification push(String deviceToken, String payload, Date expiry) throws NetworkIOException {
        PriorityApnsNotification notification =
                new PriorityApnsNotification(c.incrementAndGet(), (int)(expiry.getTime() / 1000), deviceToken, payload);
            push(notification);
            return notification;
    }

    @Override
    public ApnsNotification push(byte[] deviceToken, byte[] payload) throws NetworkIOException {
        PriorityApnsNotification notification =
                new PriorityApnsNotification(c.incrementAndGet(), EnhancedApnsNotification.MAXIMUM_EXPIRY, deviceToken, payload);
            push(notification);
            return notification;
    }

    @Override
    public EnhancedApnsNotification push(byte[] deviceToken, byte[] payload, int expiry) throws NetworkIOException {
        PriorityApnsNotification notification =
                new PriorityApnsNotification(c.incrementAndGet(), expiry, deviceToken, payload);
            push(notification);
            return notification;
    }

    @Override
    public Collection<? extends ApnsNotification> push(Collection<String> deviceTokens, String payload)
            throws NetworkIOException {
        byte[] messageBytes = Utilities.toUTF8Bytes(payload);
        List<PriorityApnsNotification> notifications = new ArrayList<PriorityApnsNotification>(deviceTokens.size());
        for (String deviceToken : deviceTokens) {
            byte[] dtBytes = Utilities.decodeHex(deviceToken);
            PriorityApnsNotification notification =
                new PriorityApnsNotification(c.incrementAndGet(), EnhancedApnsNotification.MAXIMUM_EXPIRY, dtBytes, messageBytes);
            notifications.add(notification);
            push(notification);
        }
        return notifications;
    }

    @Override
    public Collection<? extends EnhancedApnsNotification> push(Collection<String> deviceTokens, String payload,
            Date expiry) throws NetworkIOException {
        byte[] messageBytes = Utilities.toUTF8Bytes(payload);
        List<PriorityApnsNotification> notifications = new ArrayList<PriorityApnsNotification>(deviceTokens.size());
        for (String deviceToken : deviceTokens) {
            byte[] dtBytes = Utilities.decodeHex(deviceToken);
            PriorityApnsNotification notification =
                new PriorityApnsNotification(c.incrementAndGet(), (int)(expiry.getTime() / 1000), dtBytes, messageBytes);
            notifications.add(notification);
            push(notification);
        }
        return notifications;
    }

    @Override
    public Collection<? extends ApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload)
            throws NetworkIOException {
        List<PriorityApnsNotification> notifications = new ArrayList<PriorityApnsNotification>(deviceTokens.size());
        for (byte[] deviceToken : deviceTokens) {
            PriorityApnsNotification notification =
                new PriorityApnsNotification(c.incrementAndGet(), EnhancedApnsNotification.MAXIMUM_EXPIRY, deviceToken, payload);
            notifications.add(notification);
            push(notification);
        }
        return notifications;
    }

    @Override
    public Collection<? extends EnhancedApnsNotification> push(Collection<byte[]> deviceTokens, byte[] payload,
            int expiry) throws NetworkIOException {
        List<PriorityApnsNotification> notifications = new ArrayList<PriorityApnsNotification>(deviceTokens.size());
        for (byte[] deviceToken : deviceTokens) {
            PriorityApnsNotification notification =
                new PriorityApnsNotification(c.incrementAndGet(), expiry, deviceToken, payload);
            notifications.add(notification);
            push(notification);
        }
        return notifications;
    }

    @Override
    public void push(ApnsNotification message) throws NetworkIOException {
        if(message instanceof PriorityApnsNotification) {
            push((PriorityApnsNotification)message);
        } else {
            service.push(message);
            logger.warn("the apns notification is not queued !");    
        }
    }

}
