// @formatter:off
package com.everhomes.rest.user;

public enum IdentifierClaimStatus {
    FREE_STANDING((byte)0), CLAIMING((byte)1), VERIFYING((byte)2), CLAIMED((byte)3), TAKEN_OVER((byte)4); 
    
    private byte code;
    
    private IdentifierClaimStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static IdentifierClaimStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return FREE_STANDING;
            
        case 1 :
            return CLAIMING;
            
        case 2 :
            return VERIFYING;
            
        case 3 :
            return CLAIMED;
            
        case 4 :
            return TAKEN_OVER;
            
        default :
            break;
        }
        
        return null;
    }
}
