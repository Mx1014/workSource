package com.everhomes.rest.messaging;

public enum DeviceMessageType {
    SIMPLE("simple"), Jump("jump");
    
    private String code;
    
    private DeviceMessageType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DeviceMessageType fromCode(String code) {
        for(DeviceMessageType t : DeviceMessageType.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        
        return null;
    }
}
