package com.everhomes.rest.organization.pm;

public enum PmMemberStatus {
    INACTIVE((byte)0), CONFIRMING((byte)1), ACTIVE((byte)2);
    
    private byte code;
    private PmMemberStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmMemberStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return INACTIVE;
            
        case 1:
            return CONFIRMING;
            
        case 2:
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
