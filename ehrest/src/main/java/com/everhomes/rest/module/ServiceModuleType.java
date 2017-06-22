// @formatter:off
package com.everhomes.rest.module;

/**
 * <ul>
 * <li>PARK: 园区模块</li>
 * <li>ORGANIZATION: 机构企业模块</li>
 * <li>MANAGER: 运营管理方模块</li>
 * </ul>
 */
public enum ServiceModuleType {
    ORGANIZATION((byte)0), PARK((byte)1), MANAGER((byte)2);
    
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
