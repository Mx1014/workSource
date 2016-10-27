package com.everhomes.rest.organization;


public enum OrganizationMemberStatus {
	INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), WAITING_FOR_ACCEPTANCE((byte)2),  ACTIVE((byte)3), REJECT((byte)4);
    
    private byte code;
    private OrganizationMemberStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationMemberStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return INACTIVE;
            
        case 1:
            return WAITING_FOR_APPROVAL;
            
        case 2:
            return WAITING_FOR_ACCEPTANCE;
            
        case 3:
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}