package com.everhomes.rest.organization;

public enum OrganizationCommunityRequestStatus {
    INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), WAITING_FOR_ACCEPTANCE((byte)2), ACTIVE((byte)3);
    
    private byte code;
    private OrganizationCommunityRequestStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationCommunityRequestStatus fromCode(byte code) {
        for(OrganizationCommunityRequestStatus t : OrganizationCommunityRequestStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
