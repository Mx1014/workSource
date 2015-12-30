package com.everhomes.rest.messaging;

public enum PushJumpType {
    USER("U"), POST("P"), ACTIVITY("A"), GROUP("G");
    
    private String code;
    private PushJumpType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PushJumpType fromCode(String code) {
        for(PushJumpType t : PushJumpType.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        
        return null;
    }
}
