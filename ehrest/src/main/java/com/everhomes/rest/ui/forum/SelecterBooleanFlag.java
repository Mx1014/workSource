// @formatter:off
package com.everhomes.rest.ui.forum;

/**
 * <ul>选择器布尔标记
 * <li>FALSE(0): 否</li>
 * <li>TRUE(1): 是</li>
 * </ul>
 */
public enum SelecterBooleanFlag {
    FALSE((byte)0), TRUE((byte)1);
    
    private byte code;
    
    private SelecterBooleanFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SelecterBooleanFlag fromCode(Byte code) {
        if(code != null) {
            SelecterBooleanFlag[] values = SelecterBooleanFlag.values();
            for(SelecterBooleanFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
