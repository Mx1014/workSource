package com.everhomes.rest.enterprise;

public enum EnterpriseCommunityMapStatus {
    INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), WAITING_FOR_ACCEPTANCE((byte)2), ACTIVE((byte)3);
    
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
