package com.everhomes.rest.messaging.admin;

public enum SendMessageAdminTargetType {
    
    CITY(3), COMMUNITY(2), FAMILY(1), USER(0);
    
    private int code;
    private SendMessageAdminTargetType(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public static SendMessageAdminTargetType fromCode(int code) {
        for(SendMessageAdminTargetType t : SendMessageAdminTargetType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        return null;
    }
}
