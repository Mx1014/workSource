package com.everhomes.rest.activity;

public enum ActivityVideoRoomType {
    YZB("yzb");
    
    private String code;
    private ActivityVideoRoomType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ActivityVideoRoomType fromCode(String code) {
        ActivityVideoRoomType[] values = ActivityVideoRoomType.values();
        for(ActivityVideoRoomType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
