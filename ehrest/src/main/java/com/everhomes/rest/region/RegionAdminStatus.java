// @formatter:off
package com.everhomes.rest.region;

/**
 * <ul>
 * <li>INACTIVE: 无效</li>
 * <li>ACTIVE: 正常</li>
 * <li>LOCKED: 锁定</li>
 * <li>DELETED: 已删除</li>
 * </ul>
 */
public enum RegionAdminStatus {
    INACTIVE((byte)1), ACTIVE((byte)2), LOCKED((byte)3), DELETED((byte)4);
    
    private byte code;
    
    private RegionAdminStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RegionAdminStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
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
