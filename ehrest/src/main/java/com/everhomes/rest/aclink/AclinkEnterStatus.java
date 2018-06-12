package com.everhomes.rest.aclink;

public enum AclinkEnterStatus {
	OUT((byte)0), IN((byte)1);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private AclinkEnterStatus(byte code) {
        this.code = code;
    }
    
    public static AclinkEnterStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return OUT;
            
        case 1 :
            return IN;
        }
        
        return null;
    }
}
