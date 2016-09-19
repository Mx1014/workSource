package com.everhomes.rest.aclink;

public enum AclinkLogEventType {
    PHONE_BLE_OPEN(0l), PHONE_QR_OPEN(1l);
    private long code;
    
    private AclinkLogEventType(long code) {
        this.code = code;
    }
    
    public long getCode() {
        return this.code;
    }
    
    public static AclinkLogEventType fromCode(Long code) {
        if(code == null)
            return null;
        
        switch(code.intValue()) {
        case 0:
            return PHONE_BLE_OPEN;
        case 1:
            return PHONE_QR_OPEN;
        default :
            break;
        }
        
        return null;
    }
}
