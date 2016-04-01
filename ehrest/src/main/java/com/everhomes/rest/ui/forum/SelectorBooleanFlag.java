// @formatter:off
package com.everhomes.rest.ui.forum;

/**
 * <ul>选择器布尔标记
 * <li>FALSE(0): 否</li>
 * <li>TRUE(1): 是</li>
 * </ul>
 */
public enum SelectorBooleanFlag {
    FALSE((byte)0), TRUE((byte)1);
    
    private byte code;
    
    private SelectorBooleanFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SelectorBooleanFlag fromCode(Byte code) {
        if(code != null) {
            SelectorBooleanFlag[] values = SelectorBooleanFlag.values();
            for(SelectorBooleanFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
