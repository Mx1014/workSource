package com.everhomes.pusher;

import com.everhomes.rest.messaging.MessagingPriorityConstants;
import com.everhomes.util.StringHelper;
import com.notnoop.apns.EnhancedApnsNotification;

public class PriorityApnsNotification extends EnhancedApnsNotification implements Comparable<PriorityApnsNotification> {

    int priority;
    long startTick;

    public PriorityApnsNotification(int identifier, int expiryTime, byte[] dtoken, byte[] payload) {
        super(identifier, expiryTime, dtoken, payload);
        this.priority = MessagingPriorityConstants.MEDIUM.getCode();
        this.startTick = System.currentTimeMillis(); 
    }
    
    public PriorityApnsNotification(int identifier, int expiryTime, byte[] dtoken, byte[] payload, int priority) {
        super(identifier, expiryTime, dtoken, payload);
        this.priority = priority;
        this.startTick = System.currentTimeMillis(); 
    }
    
    public PriorityApnsNotification(int incrementAndGet, int maximumExpiry, String deviceToken, String payload) {
        super(incrementAndGet, maximumExpiry, deviceToken, payload);
        this.priority = MessagingPriorityConstants.MEDIUM.getCode();
        this.startTick = System.currentTimeMillis(); 
    }
    
    public PriorityApnsNotification(int incrementAndGet, int maximumExpiry, String deviceToken, String payload, int priority) {
        super(incrementAndGet, maximumExpiry, deviceToken, payload);
        this.priority = priority;
        this.startTick = System.currentTimeMillis(); 
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public long getStartTick() {
        return startTick;
    }
    public void setStartTick(long startTick) {
        this.startTick = startTick;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
    @Override
    public int compareTo(PriorityApnsNotification o) {
        return Long.compare(this.getPriority(), o.getPriority());
    }
}
