package com.everhomes.rest.organization;
public enum OrganizationStatus {
    UNTREATED((byte)0), INACTIVE((byte)1), ACTIVE((byte)2), LOCKED((byte)3), DELETED((byte)4);
    
    private byte code;
    private OrganizationStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return UNTREATED;
            
        case 1:
            return INACTIVE;
            
        case 2:
            return ACTIVE;
            
        case 3:
            return LOCKED;
            
        case 4:
            return DELETED;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}