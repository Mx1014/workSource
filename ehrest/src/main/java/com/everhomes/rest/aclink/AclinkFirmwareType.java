package com.everhomes.rest.aclink;

public enum AclinkFirmwareType {
    ZUOLIN("zuolin");
    
    private String code;
    private AclinkFirmwareType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static AclinkFirmwareType fromCode(String code) {
        AclinkFirmwareType[] values = AclinkFirmwareType.values();
        for(AclinkFirmwareType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
