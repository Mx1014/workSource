package com.everhomes.rest.activity;

public enum ActivityVideoSupportType {
    NO((byte)0), YES((byte)1);
    
    private Byte code;
    private ActivityVideoSupportType(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static ActivityVideoSupportType fromCode(Byte code) {
        ActivityVideoSupportType[] values = ActivityVideoSupportType.values();
        for(ActivityVideoSupportType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
