package com.everhomes.rest.dbsync;

public enum SyncStateType {
    INVALID((byte)0), SLEEPING((byte)1), PROCESSING((byte)2), CONFLICT((byte)3);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private SyncStateType(byte code) {
        this.code = code;
    }
    
    public static SyncStateType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
            return SLEEPING;
        case 2 :
            return PROCESSING;
        case 3 :
            return CONFLICT;
        }
        
        return null;
    }
}
