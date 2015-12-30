// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>参数类型
 * <li>JSON: json字符串</li>
 * <li>ENTITY: 实体类型</li>
 * </ul>
 */
public enum ItemKind {
    JSON((byte)0), ENTITY((byte)1);
    
    private byte code;
    private ItemKind(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ItemKind fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return JSON;
            
        case 1:
            return ENTITY;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
