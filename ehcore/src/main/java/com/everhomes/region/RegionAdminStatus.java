// @formatter:off
package com.everhomes.region;

public enum RegionAdminStatus {
    INACTIVE((byte)1), ACTIVE((byte)2), LOCKED((byte)3), DELETED((byte)4);
    
    private byte code;
    
    private RegionAdminStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RegionAdminStatus fromCode(byte code) {
        switch(code) {
        case 1:
            return INACTIVE;
            
        case 2:
            return ACTIVE;
            
        case 3:
            return LOCKED;
            
        case 4:
            return DELETED;
            
        default :
            assert(false);
            break;
        }
        return null;
    }
}
