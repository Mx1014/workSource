package com.everhomes.rest.enterprise;

public enum EnterpriseContactEntryType {
    Email((byte)1), Mobile((byte)0);
    
    private byte code;
    private EnterpriseContactEntryType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseContactEntryType fromCode(byte code) {
        for(EnterpriseContactEntryType t : EnterpriseContactEntryType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
