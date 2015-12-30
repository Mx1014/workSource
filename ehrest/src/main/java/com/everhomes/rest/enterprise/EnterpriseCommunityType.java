package com.everhomes.rest.enterprise;

public enum EnterpriseCommunityType {
    Enterprise((byte)1), Normal((byte)0);
    
    private byte code;
    private EnterpriseCommunityType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseCommunityType fromCode(byte code) {
        for(EnterpriseCommunityType t : EnterpriseCommunityType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
