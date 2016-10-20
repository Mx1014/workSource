package com.everhomes.rest.messaging;

public enum MessagingPriorityConstants {
    HIGH(100),
    MEDIUM(10),
    LOW(1);
    
    private int code;
    private MessagingPriorityConstants(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
