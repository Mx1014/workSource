package com.everhomes.rest.aclink;

public enum DoorAuthType {
    FOREVER((byte)0), TEMPERATE((byte)1);
    private byte code;
    
    private DoorAuthType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DoorAuthType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return FOREVER;
        case 1 :
            return TEMPERATE;
        default :
            break;
        }
        
        return null;
    }
}
