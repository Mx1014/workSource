// @formatter:off
package com.everhomes.address;

public enum AddressClaimType {
    FAMILY_RESIDENT((byte)0), COMMERCIAL_ENTITY((byte)1);
    
    private byte code;
    
    private AddressClaimType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AddressClaimType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return FAMILY_RESIDENT;
            
        case 1 :
            return COMMERCIAL_ENTITY;
            
        default :
            assert(false);
            break;
        }
                    
        return null;           
    }
}
