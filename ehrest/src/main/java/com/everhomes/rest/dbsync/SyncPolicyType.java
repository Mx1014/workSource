package com.everhomes.rest.dbsync;

public enum SyncPolicyType {
    REFRESH((byte)0), INCREMENT((byte)1);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private SyncPolicyType(byte code) {
        this.code = code;
    }
    
    public static SyncPolicyType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return REFRESH;
        case 1 :
            return INCREMENT;
        }
        
        return null;
    }
}
