// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>
 * <li>HIDE(0): 隐藏</li>
 * <li>DISPLAY(1): 显示</li>
 * </ul>
 */
public enum ItemDisplayFlag {
    HIDE((byte)0), DISPLAY((byte)1);
    
    private byte code;
    private ItemDisplayFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ItemDisplayFlag fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return HIDE;
            
        case 1:
            return DISPLAY;

        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
