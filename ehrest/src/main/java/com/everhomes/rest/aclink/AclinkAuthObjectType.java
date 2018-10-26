package com.everhomes.rest.aclink;

public enum AclinkAuthObjectType {
	USER((byte) 0), ORGANIZATION((byte) 1), COMPANY_ADDRESS((byte) 2), FAMILY_ADDRESS((byte) 3);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private AclinkAuthObjectType(byte code) {
        this.code = code;
    }
    
    public static AclinkAuthObjectType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return USER;
            
        case 1 :
            return ORGANIZATION;
            
        case 2:
        	return COMPANY_ADDRESS;
        	
        case 3:
        	return FAMILY_ADDRESS;
        }
        
        return null;
    }
}
