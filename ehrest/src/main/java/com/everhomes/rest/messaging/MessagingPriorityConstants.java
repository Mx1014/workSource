package com.everhomes.rest.messaging;

public enum MessagingPriorityConstants {
    HIGH(1),
    MEDIUM(10),
    LOW(100);
    
    private int code;
    private MessagingPriorityConstants(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
