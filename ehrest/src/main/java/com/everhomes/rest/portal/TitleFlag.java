// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>FALSE: false</li>
 * <li>TRUE: true</li>
 * </ul>
 */
public enum TitleFlag {
    FALSE((byte)0), TRUE((byte)1);

    private byte code;

    private TitleFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static TitleFlag fromCode(Byte code) {
        if(null != code){
            for (TitleFlag value: TitleFlag.values()) {
                if(value.code == code.byteValue()){
                    return value;
                }
            }
        }
        return null;
    }
}
