// @formatter:off
package com.everhomes.rest.module;

/**
 * <ul>
 * <li>INACTIVE: 无效的</li>
 * <li>CONFIRMING: 待审核</li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum ServiceModuleStatus {
    INACTIVE((byte)0), CONFIRMING((byte)1), ACTIVE((byte)2);
    
    private byte code;
    
    private ServiceModuleStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ServiceModuleStatus fromCode(Byte code) {
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
            break;
        }
        return null;
    }
}
