// @formatter:off
package com.everhomes.rest.address;

/**
 * <p>地址状态:</p>
 * <ul>
 * <li>INACTIVE: 无效 </li>
 * <li>CONFIRMING: 待审核 </li>
 * <li>ACTIVE: 正常 </li>
 * </ul>
 */
public enum AddressAdminStatus {
    INACTIVE((byte)0), CONFIRMING((byte)1),ACTIVE((byte)2);
    
    private byte code;
    private AddressAdminStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AddressAdminStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INACTIVE;
            
        case 1 :
            return CONFIRMING;
            
        case 2 :
            return ACTIVE;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
