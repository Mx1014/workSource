package com.everhomes.rest.enterprise;

public enum EnterpriseCommunityStatus {
    Approving((byte)2), Approved((byte)1), Inactive((byte)0);
    
    private byte code;
    private EnterpriseCommunityStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseCommunityStatus fromCode(byte code) {
        for(EnterpriseCommunityStatus t : EnterpriseCommunityStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
