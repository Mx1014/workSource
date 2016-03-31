package com.everhomes.aclink;

public enum DoorAccessOwnerType {
    COMMUNITY((byte)0), ENTERPRISE((byte)1);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private DoorAccessOwnerType(byte code) {
        this.code = code;
    }
    
    public static DoorAccessOwnerType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return COMMUNITY;
            
        case 1 :
            return ENTERPRISE;
        }
        
        return null;
    }
}
