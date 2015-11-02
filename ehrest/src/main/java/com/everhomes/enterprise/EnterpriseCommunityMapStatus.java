package com.everhomes.enterprise;

public enum EnterpriseCommunityMapStatus {
    Inviting((byte)3), Approving((byte)2), Approved((byte)1), Inactive((byte)0);
    
    private byte code;
    private EnterpriseCommunityMapStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseCommunityMapStatus fromCode(byte code) {
        for(EnterpriseCommunityMapStatus t : EnterpriseCommunityMapStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
