// @formatter:off
package com.everhomes.pkg;

public enum ClientPackageStatus {
    INACTIVE((byte)0), ACTIVE((byte)1);
    
    private byte code;
    private ClientPackageStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ClientPackageStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return INACTIVE;
            
        case 1:
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
