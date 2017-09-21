// @formatter:off
package com.everhomes.rest.module;

/**
 * <ul>
 * <li>NO: 没有</li>
 * <li>YES: 有</li>
 * </ul>
 */
public enum ServiceModuleActionTypeFlag {
    NO((byte)0), YES((byte)1);

    private byte code;

    private ServiceModuleActionTypeFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ServiceModuleActionTypeFlag fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return NO;
            
        case 1 :
            return YES;

        default :
            break;
        }
        return null;
    }
}
