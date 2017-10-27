package com.everhomes.messaging;

public enum PusherVenderType {
    HUAWEI("huawei"),
    XIAOMI("xiaomi")
    ;
    
    private String code;

    PusherVenderType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PusherVenderType fromCode(String code) {
        for(PusherVenderType t : PusherVenderType.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        return null;
    }
}
