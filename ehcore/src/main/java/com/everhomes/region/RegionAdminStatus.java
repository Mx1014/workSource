// @formatter:off
package com.everhomes.region;

public enum RegionAdminStatus {
    inactive((byte)1), active((byte)2), locked((byte)3), deleted((byte)4);
    
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
            return inactive;
        case 2:
            return active;
            
        case 3:
            return locked;
            
        case 4:
            return deleted;
            
        default :
            assert(false);
            break;
        }
        return null;
    }
}
