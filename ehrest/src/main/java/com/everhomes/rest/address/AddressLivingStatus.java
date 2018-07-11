// @formatter:off
package com.everhomes.rest.address;

/**
 * <p>地址入住状态</p>
 * <ul>
 * <li>INACTIVE: 无人 入住</li>
 * <li>ACTIVE: 有人入住</li>
 * </ul>
 */
public enum AddressLivingStatus {
    // 枚举缺少数据，还有为3等的情况，java文件和数据库没有记录，只有web端有注释记录 by wentian
    INACTIVE((byte)0), ACTIVE((byte)1), OCCUPIED((byte)6);
    
    private byte code;
    AddressLivingStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AddressLivingStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INACTIVE;
            
        case 1 :
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
