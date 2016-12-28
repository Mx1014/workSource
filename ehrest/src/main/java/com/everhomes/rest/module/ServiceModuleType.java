// @formatter:off
package com.everhomes.rest.module;

/**
 * <ul>
 * <li>INACTIVE: 无效的</li>
 * <li>CONFIRMING: 待审核</li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum ServiceModuleType {
    PARK((byte)0), ORGANIZATION((byte)1), MANAGER((byte)2);
    
    private byte code;
    
    private ServiceModuleType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ServiceModuleType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return PARK;
            
        case 1 :
            return ORGANIZATION;
            
        case 2 :
            return MANAGER;
        
        default :
            break;
        }
        return null;
    }
}
