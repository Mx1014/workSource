package com.everhomes.rest.aclink;

public enum AclinkLogEventType {
    PHONE_BLE_OPEN(0l), PHONE_QR_OPEN(1l), PHONE_REMOTE_OPEN(2l), BUTTON_OPEN(3l), FACE_OPEN(4l);
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
        case 2:
        	return PHONE_REMOTE_OPEN;
        case 3:
        	return BUTTON_OPEN;
        case 4:
            return FACE_OPEN;
        default :
            break;
        }
        
        return null;
    }
}
