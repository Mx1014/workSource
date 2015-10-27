package com.everhomes.enterprise;

public enum EnterpriseContactStatus {
    Inactive((byte)2), Approved((byte)1), Approving((byte)0);
    
    private byte code;
    private EnterpriseContactStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseContactStatus fromCode(byte code) {
        for(EnterpriseContactStatus t : EnterpriseContactStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
