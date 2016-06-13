package com.everhomes.aclink;

public enum DoorCommandStatus {
    CREATING((byte)0x0), SENDING((byte)0x1), RESPONSE((byte)0x2), PROCESS((byte)0x3), INVALID((byte)0x4);
    
    private byte code;
    
    private DoorCommandStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DoorCommandStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return CREATING;
        case 1:
            return SENDING;
        case 2:
            return RESPONSE;
        case 3:
            return PROCESS;
        case 4:
            return INVALID;
        default :
            break;
        }
        
        return null;
    }
}
