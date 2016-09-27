package com.everhomes.rest.activity;

public enum VideoManufacturerType {
    YZB("yzb");
    
    private String code;
    private VideoManufacturerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static VideoManufacturerType fromCode(String code) {
        VideoManufacturerType[] values = VideoManufacturerType.values();
        for(VideoManufacturerType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
