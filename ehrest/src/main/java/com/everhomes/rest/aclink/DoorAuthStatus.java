package com.everhomes.rest.aclink;

public enum DoorAuthStatus {
    INVALID((byte)0), VALID((byte)1);
    
    private byte code;
    
    private DoorAuthStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DoorAuthStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
            return VALID;
        default :
            break;
        }
        
        return null;
    }
}
