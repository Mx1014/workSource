// @formatter:off
package com.everhomes.address;

public enum AddressClaimStatus {
    UNCLAIMED((byte)0), CLAIMING((byte)1), CLAIMED((byte)2), REJECTED((byte)3);
    
    private byte code;
    private AddressClaimStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AddressClaimStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return UNCLAIMED;
            
        case 1:
            return CLAIMING;
            
        case 2:
            return CLAIMED;
            
        case 3:
            return REJECTED;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
