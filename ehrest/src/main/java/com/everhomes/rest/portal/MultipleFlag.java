// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>NO: 不是多入口</li>
 * <li>YES: 是多入口</li>
 * </ul>
 */
public enum MultipleFlag {
    NO((byte)0), YES((byte)1);

    private byte code;

    private MultipleFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static MultipleFlag fromCode(Byte code) {
        if(null != code){
            for (MultipleFlag value: MultipleFlag.values()) {
                if(value.code == code.byteValue()){
                    return value;
                }
            }
        }
        return null;
    }
}
