package com.everhomes.rest.aclink;

public enum DoorAccessStatus {
    ACTIVING((byte)0), ACTIVE((byte)1), INVALID((byte)2);
    
    private byte code;
    
    private DoorAccessStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DoorAccessStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return ACTIVING;
        case 1 :
            return ACTIVE;
        case 2:
            return INVALID;
        default :
            break;
        }
        
        return null;
    }
}
