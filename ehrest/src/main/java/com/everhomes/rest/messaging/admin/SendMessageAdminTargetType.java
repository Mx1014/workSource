package com.everhomes.rest.messaging.admin;

public enum SendMessageAdminTargetType {
    
    CITY(3l), COMMUNITY(2l), FAMILY(1l), USER(0l);
    
    private long code;
    private SendMessageAdminTargetType(long code) {
        this.code = code;
    }
    
    public Long getCode() {
        return this.code;
    }
    
    public static SendMessageAdminTargetType fromCode(long code) {
        for(SendMessageAdminTargetType t : SendMessageAdminTargetType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
