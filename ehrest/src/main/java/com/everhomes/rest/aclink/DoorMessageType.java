package com.everhomes.rest.aclink;

public enum DoorMessageType {
    NORMAL((byte)0);
    private byte code;
    
    private DoorMessageType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DoorMessageType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return NORMAL;
        default :
            break;
        }
        
        return null;
    }
}
