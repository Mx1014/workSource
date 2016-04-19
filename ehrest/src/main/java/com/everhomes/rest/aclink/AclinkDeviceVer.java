package com.everhomes.rest.aclink;

public enum AclinkDeviceVer {
    VER0((byte)0), VER1((byte)1);
    
    private byte code;
    
    private AclinkDeviceVer(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AclinkDeviceVer fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return VER0;
        case 1 :
            return VER1;
        default :
            break;
        }
        
        return null;
    }
}
