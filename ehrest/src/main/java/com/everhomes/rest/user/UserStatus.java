// @formatter:off
package com.everhomes.rest.user;

public enum UserStatus {
    INACTIVE((byte)0), ACTIVE((byte)1);
    
    private byte code;
    
    private UserStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static UserStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INACTIVE;
            
        case 1 :
            return ACTIVE;
            
        default :
            break;
        }
        
        return null;
    }
}
