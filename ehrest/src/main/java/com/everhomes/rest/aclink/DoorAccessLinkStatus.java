package com.everhomes.rest.aclink;

public enum DoorAccessLinkStatus {
    SUCCESS((byte)0), FAILED((byte)1);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private DoorAccessLinkStatus(byte code) {
        this.code = code;
    }
    
    public static DoorAccessLinkStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return SUCCESS;
            
        case 1 :
            return FAILED;
        }
        
        return null;
    }
}
