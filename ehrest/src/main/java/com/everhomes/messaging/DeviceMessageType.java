package com.everhomes.messaging;

public enum DeviceMessageType {
    SIMPLE("simple");
    
    private String code;
    
    private DeviceMessageType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DeviceMessageType fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase("simple"))
            return SIMPLE;
        
        return null;
    }
}
