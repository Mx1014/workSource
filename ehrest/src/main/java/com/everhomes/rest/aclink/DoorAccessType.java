package com.everhomes.rest.aclink;

public enum DoorAccessType {
    ZLACLINK_WIFI((byte)0), ZLACLINK_NOWIFI((byte)1);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private DoorAccessType(byte code) {
        this.code = code;
    }
    
    public static DoorAccessType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return ZLACLINK_WIFI;
            
        case 1 :
            return ZLACLINK_NOWIFI;
        }
        
        return null;
    }
}
