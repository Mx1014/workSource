// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>策略
 * <li>DEFAULT(0): 默认</li>
 * <li>OVERRIDE(1): 覆盖</li>
 * <li>REVERT(2): 恢复</li>
 * </ul>
 */
public enum ApplyPolicy {
    DEFAULT((byte)0), OVERRIDE((byte)1), REVERT((byte)2);
    
    private byte code;
    private ApplyPolicy(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ApplyPolicy fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return DEFAULT;
            
        case 1:
            return OVERRIDE;
        
        case 2: 
            return REVERT;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
