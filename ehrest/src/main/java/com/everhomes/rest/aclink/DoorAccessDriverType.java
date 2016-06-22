package com.everhomes.rest.aclink;

public enum DoorAccessDriverType {
    ZUOLIN("zuolin"), LINGLING("lingling");
    
    private String code;
    private DoorAccessDriverType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DoorAccessDriverType fromCode(String code) {
        DoorAccessDriverType[] values = DoorAccessDriverType.values();
        for(DoorAccessDriverType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
