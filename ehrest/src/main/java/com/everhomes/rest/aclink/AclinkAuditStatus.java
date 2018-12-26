package com.everhomes.rest.aclink;

public enum AclinkAuditStatus {
	INVALID((byte)0), SUCCESS((byte)1), WAITING((byte)2), REJECT((byte)3);
    
    private byte code;
    
    private AclinkAuditStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AclinkAuditStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
            return SUCCESS;
        case 2:
            return WAITING;
        case 3:
            return REJECT;
        default :
            break;
        }
        
        return null;
    }
}
