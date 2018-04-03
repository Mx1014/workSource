// @formatter:off
package com.everhomes.rest.module;

/**
 * <ul>
 * </ul>
 */
public enum ServiceModuleScopeApplyPolicy {
    DEFAULT((byte)0), OVERRIDE((byte)1), REVERT((byte)2)/*, CUSTOMIZED((byte)3)*/;
    
    private byte code;
    
    private ServiceModuleScopeApplyPolicy(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ServiceModuleScopeApplyPolicy fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return DEFAULT;
            
        case 1 :
            return OVERRIDE;
            
        case 2 :
            return REVERT;
        
        default :
            break;
        }
        return null;
    }
}
