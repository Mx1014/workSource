package com.everhomes.enterprise;

public enum EnterpriseGroupMemberStatus {
    Approving((byte)2), Approved((byte)1), Inactive((byte)0);
    
    private byte code;
    private EnterpriseGroupMemberStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseGroupMemberStatus fromCode(byte code) {
        for(EnterpriseGroupMemberStatus t : EnterpriseGroupMemberStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
