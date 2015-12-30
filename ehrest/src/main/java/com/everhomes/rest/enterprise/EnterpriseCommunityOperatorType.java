package com.everhomes.rest.enterprise;

public enum EnterpriseCommunityOperatorType {
    InviteToJoin((byte)2), RequestToJoin((byte)1);
    
    private byte code;
    private EnterpriseCommunityOperatorType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseCommunityOperatorType fromCode(byte code) {
        for(EnterpriseCommunityOperatorType t : EnterpriseCommunityOperatorType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
