package com.everhomes.aclink;

public enum DoorAuthMethodType {
    MOBILE("mobile"), ADMIN("admin");
    
    private String code;
    private DoorAuthMethodType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DoorAuthMethodType fromCode(String code) {
        DoorAuthMethodType[] values = DoorAuthMethodType.values();
        for(DoorAuthMethodType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
