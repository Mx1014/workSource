package com.everhomes.rest.organization.pm;

public enum OrganizationScopeCode { 

	NONE((byte)0), BUILDING((byte)1);
	
	private byte code;
    private OrganizationScopeCode(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationScopeCode fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return NONE;
            
        case 1:
            return BUILDING;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
